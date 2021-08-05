FROM node:14

WORKDIR /usr/src/app

COPY package.json ./
RUN npm install pm2 -g
RUN npm install --only=production

COPY . .

EXPOSE 3000
CMD [ "npm", "start" ]