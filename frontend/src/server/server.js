import path from 'path';
import { fileURLToPath } from 'url';

import cors from 'cors';
import express from 'express';
import signGenerator from 'sign-generator';
// eslint-disable-next-line @typescript-eslint/naming-convention, no-underscore-dangle
const __filename = fileURLToPath(import.meta.url);
// eslint-disable-next-line @typescript-eslint/naming-convention, no-underscore-dangle
const __dirname = path.dirname(__filename);
const { createStamp } = signGenerator;

const app = express();
const port = 3002;

app.use(cors());

// 프로젝트 루트 디렉토리 찾기
const projectRoot = path.resolve(__dirname, '..');

app.get('/sign/:text', async (req, res) => {
  try {
    const stampItems = await createStamp(req.params.text, 'fonts/HJ.ttf');
    const lastStamp = stampItems[stampItems.length - 1];
    res.status(200).send(lastStamp);
  } catch (e) {
    res
      .status(500)
      .send({ error: 'Failed to generate sign', details: e.message });
  }
});

// Serve static files from the React app
const distPath = path.join(projectRoot, 'dist');
app.use(express.static(distPath));

// The "catchall" handler: for any request that doesn't
// match one above, send back React's index.html file.
app.get('*', (req, res) => {
  res.sendFile(path.join(distPath, 'index.html'));
});

app.listen(port);
