import DetailClient from './DetailClient';

// 静的パラメータの生成
export async function generateStaticParams() {
  // すべての星座のパスを生成
  const zodiacs = [
    'aries',
    'taurus',
    'gemini',
    'cancer',
    'leo',
    'virgo',
    'libra',
    'scorpio',
    'sagittarius',
    'capricorn',
    'aquarius',
    'pisces'
  ];

  return zodiacs.map((zodiac) => ({
    zodiac: zodiac,
  }));
}

interface DetailPageProps {
  params: {
    zodiac: string;
  };
}

export default function DetailPage({ params }: DetailPageProps) {
  return <DetailClient zodiacName={params.zodiac} />;
}