FROM node:18-slim
WORKDIR /app
ENV PORT 8080
# Install ffmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    rm -rf /var/lib/apt/lists/*
COPY . .
RUN npm install -g npm@latest && npm install --omit=dev
EXPOSE 8080
CMD [ "npm", "run", "start"]