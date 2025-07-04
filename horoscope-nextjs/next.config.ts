import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  output: 'export',
  basePath: process.env.NODE_ENV === 'production' ? '/shota-koga-0702' : '',
  images: {
    unoptimized: true,
  },
};

export default nextConfig;
