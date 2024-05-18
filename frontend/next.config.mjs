import { config } from 'dotenv';

config();

const nextConfig = {
    env: {
        BACKEND_ADDRESS: process.env.BACKEND_ADDRESS,
        BACKEND_PORT: process.env.BACKEND_PORT,
    },
};

export default nextConfig;