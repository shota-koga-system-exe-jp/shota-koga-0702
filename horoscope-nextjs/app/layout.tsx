import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "今日の星座占い",
  description: "12星座の今日の運勢をチェック",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ja">
      <body>
        {children}
      </body>
    </html>
  );
}
