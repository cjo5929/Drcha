from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from llama_cpp import Llama
import logging
import asyncio
from functools import lru_cache

from typing import Dict, Any

app = FastAPI()
logging.basicConfig(level=logging.DEBUG)

MODEL_POOL_SIZE = 4

def setup_model():
    return Llama(
        model_path='./model/llama-3-Korean-Bllossom-8B-Q4_K_M.gguf',
        n_ctx=512,
        n_gpu_layers=20,
        n_threads=1,
        use_mlock=True,
        use_mmap=True,
        offload_kqv=True,
        logits_all=False,
        embedding=False,
        verbose=False
    )

model_pool = [setup_model() for _ in range(MODEL_POOL_SIZE)]

class ConversationInput(BaseModel):
    conversation: str

@lru_cache(maxsize=100)
def generate_response(model: Llama, prompt: str):
    try:
        response = model(prompt, max_tokens=100, stop=["대화:", "\n\n"], echo=False)
        return response
    except Exception as e:
        logging.error(f"Error generating response: {e}")
        return None

@app.post('/extract')
async def extract_loan_info(input_data: ConversationInput):
    extract_prompt = f'''다음 대화에서 대출 정보를 추출하세요:
대화:
{input_data.conversation}

다음 형식으로 정확히 정보를 추출해주세요. 불필요한 설명은 하지 마세요.:
iouAmount: [금액]
interestRate: [이자율]
contractEndDate: [상환일 (날짜로)]

추출된 정보:'''

    loop = asyncio.get_event_loop()
    response = await loop.run_in_executor(None, generate_response, model_pool[0], extract_prompt)

    if response is None:
        raise HTTPException(status_code=500, detail="Failed to generate response")

    # 반환된 응답을 파싱하여 JSON 형식으로 변환
    try:
        lines = response['choices'][0]['text'].strip().splitlines()
        extracted_info = {
            "iouAmount": None,
            "interestRate": None,
            "contractEndDate": None
        }

        for line in lines:
            if "iouAmount:" in line:
                extracted_info["iouAmount"] = line.split("iouAmount:")[1].strip()
            elif "interestRate:" in line:
                extracted_info["interestRate"] = line.split("interestRate:")[1].strip()
            elif "contractEndDate:" in line:
                extracted_info["contractEndDate"] = line.split("contractEndDate:")[1].strip()

        return extracted_info

    except Exception as e:
        logging.error(f"Error parsing response: {e}")
        raise HTTPException(status_code=500, detail="Failed to parse response")

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
