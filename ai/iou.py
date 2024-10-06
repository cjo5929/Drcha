import os
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import openai
import logging
import asyncio
from datetime import datetime
from fastapi.middleware.cors import CORSMiddleware



app = FastAPI()
# CORS 설정 추가
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
openai.api_key = os.getenv("OPENAI_API_KEY")  # 환경 변수에서 OpenAI API 키 가져오기


class ConversationInput(BaseModel):
    conversation: str


async def generate_response_gpt3_5(prompt: str) -> str:
    try:
        logging.debug(f"Generating response for prompt: {prompt}")
        loop = asyncio.get_running_loop()
        response = await loop.run_in_executor(None, lambda: openai.ChatCompletion.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=100,
            stop=["대화:", "\n\n"]
        ))
        logging.debug(f"OpenAI API raw response: {response}")
        return response['choices'][0]['message']['content'].strip()
    except Exception as e:
        logging.error(f"Error generating response: {e}")
        return None


@app.post('/extract')
async def extract_loan_info(input_data: ConversationInput):
    logging.debug(f"Received input data: {input_data}")

    current_date = datetime.now().strftime("%Y년 %m월 %d일")

    extract_prompt = (f'다음 대화에서 대출 정보를 추출하세요:\n'
                      f'대화:\n'
                      f'{input_data.conversation}\n'
                      f'\n'
                      f'다음 형식으로 정확히 정보를 추출해주세요. 불필요한 설명은 하지 마세요. :\n'
                      f'iouAmount: 금액을 숫자로 변환해서 보내줘. 예를 들어 "300만원"은 "3000000"으로 변환해줘.\n'
                      f'interestRate: 이자율은 % 기호 없이 숫자로만 보내줘. 이자율이 없으면 0으로 설정해줘.\n'
                      f'contractEndDate: 상환일은 "yyyy-MM-dd HH:mm:ss" 형식으로 날짜로 표시해줘. 만약 "6개월 후"나 "2주 후"와 같이 상대적인 날짜가 나오면 현재 날짜 "{current_date}"를 기준으로 계산해서 정확한 날짜로 변환해줘.\n'
                      f'\n'
                      f'iouAmount: [금액]\n'
                      f'interestRate: [이자율]\n'
                      f'contractEndDate: [상환일 (yyyy-MM-dd HH:mm:ss)]\n'
                      f'\n'
                      f'추출된 정보:')

    response = await generate_response_gpt3_5(extract_prompt)

    if response is None:
        raise HTTPException(status_code=500, detail="Failed to generate response")

    logging.debug(f"Generated response from OpenAI: {response}")

    try:
        lines = response.splitlines()
        extracted_info = {
            "iouAmount": None,
            "interestRate": None,
            "contractEndDate": None
        }

        for line in lines:
            if "iouAmount:" in line:
                extracted_info["iouAmount"] = line.split("iouAmount:")[1].strip()
            elif "interestRate:" in line:
                interest_rate = line.split("interestRate:")[1].strip()
                extracted_info["interestRate"] = interest_rate.replace('%', '').strip()

            elif "contractEndDate:" in line:
                extracted_info["contractEndDate"] = line.split("contractEndDate:")[1].strip()

        logging.debug(f"Extracted loan information: {extracted_info}")

        return extracted_info

    except Exception as e:
        logging.error(f"Error parsing response: {e}")
        raise HTTPException(status_code=500, detail="Failed to parse response")


if __name__ == '__main__':
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)
