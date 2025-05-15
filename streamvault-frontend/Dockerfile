FROM node:18
WORKDIR /app
ARG API_URL=http://localhost:8080/
ENV VITE_API_URL=$API_URL
COPY package.json .
RUN npm install
COPY . .
RUN npm run build
FROM nginx:alpine
COPY --from=0 /app/dist /usr/share/nginx/html
EXPOSE 80
