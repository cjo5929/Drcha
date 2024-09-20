from flask import Flask, request, jsonify
from llama_cpp import Llama
import torch
import psutil
import GPUtil
from functools import lru_cache
import logging
import re

app = Flask(__name__)
logging.basicConfig(level=logging.DEBUG)

def check_gpu_usage():
    if torch.cuda.is_available():
        gpu = GPUtil.getGPUs()[0]
        return {
            "GPU Name": gpu.name,
            "GPU Memory Total": f"{gpu.memoryTotal} MB",
            "GPU Memory Used": f"{gpu.memoryUsed} MB",
            "GPU Memory Free": f"{gpu.memoryFree} MB",
            "GPU Utilization": f"{gpu.load * 100}%"
        }
    return {"GPU Status": "Not Available"}

def setup_model():
    logging.info("Setting up model...")
    return Llama(
        model_path='./model/llama-3-Korean-Bllossom-8B-Q4_K_M.gguf',
        n_ctx=512,
        n_gpu_layers=-1,
        n_threads=8,
        use_mlock=True,
        use_mmap=True,
        offload_kqv=True,
        logits_all=False,
        embedding=False
    )

logging.info("모델 로딩 중...")
model = setup_model()
logging.info("모델 로딩 완료")

logging.info("\nGPU 사용 상태 (모델 로딩 후):")
gpu_info = check_gpu_usage()
for key, value in gpu_info.items():
    logging.info(f"{key}: {value}")

@lru_cache(maxsize=100)
def generate_response(prompt):
    try:
        logging.debug(f"Generating response for prompt: {prompt}")
        response = model(prompt, max_tokens=100, stop=["대화:", "\n\n"], echo=False)
        logging.debug(f"Generated response: {response}")
        return response
    except Exception as e:
        logging.error(f"Error generating response: {str(e)}")
        return None

def parse_loan_info(text):
    info = {}
    lines = text.split('\n')
    for line in lines:
        if '대출금액:' in line:
            info['대출금액'] = line.split('대출금액:')[-1].strip()
        elif '이자율:' in line:
            info['이자율'] = line.split('이자율:')[-1].strip()
        elif '상환일:' in line:
            info['상환일'] = line.split('상환일:')[-1].strip()
        elif '상환방법:' in line:
            info['상환방법'] = line.split('상환방법:')[-1].strip()
    return info

@app.route('/extract', methods=['POST'])
def extract_loan_info():
    try:
        data = request.json
        conversation = data.get('conversation', '')
        logging.info(f"Received conversation: {conversation}")

        extract_prompt = f'''다음 대화에서 대출 정보를 추출하세요. 정확한 숫자와 날짜를 포함해 주세요.

대화:
{conversation}

다음 형식으로 정보를 추출해주세요:
대출금액: [금액]
이자율: [이자율]
상환일: [상환일]
상환방법: [상환방법]

추출된 정보:'''

        logging.debug(f"Extraction prompt: {extract_prompt}")
        response = generate_response(extract_prompt)

        if response is None:
            return jsonify({"error": "Failed to generate response", "GPU_Usage": check_gpu_usage()})

        extracted_info = response['choices'][0]['text'].strip()
        logging.info(f"Extracted info: {extracted_info}")

        # 추출된 정보를 딕셔너리로 변환
        info_dict = parse_loan_info(extracted_info)
        logging.debug(f"Parsed info dict: {info_dict}")

        # GPU 사용 정보 추가
        info_dict['GPU_Usage'] = check_gpu_usage()

        return jsonify(info_dict)
    except Exception as e:
        logging.error(f"Error in extract_loan_info: {str(e)}")
        return jsonify({"error": str(e), "GPU_Usage": check_gpu_usage()})

if __name__ == '__main__':
    app.run(debug=True)