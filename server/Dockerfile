FROM node:18-slim
WORKDIR /app
ENV PORT 8080
COPY . .
RUN npm install -g npm@latest && npm install --omit=dev
EXPOSE 8080
CMD [ "npm", "run", "start"]