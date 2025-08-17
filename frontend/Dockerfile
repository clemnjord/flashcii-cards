FROM node:22-slim

# Set destination for COPY
WORKDIR /app

# Download npm packages
COPY package*.json ./
RUN npm install

# Copy the source code.
COPY . .

EXPOSE 3000
CMD npm run dev