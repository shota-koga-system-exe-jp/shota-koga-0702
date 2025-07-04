import { getDailyHoroscope } from '@/lib/horoscope-service';
import Link from 'next/link';
import StarryBackground from '@/components/StarryBackground';

// 星座シンボルのマッピング
const zodiacSymbols: { [key: string]: string } = {
  'ARIES': '♈',
  'TAURUS': '♉',
  'GEMINI': '♊',
  'CANCER': '♋',
  'LEO': '♌',
  'VIRGO': '♍',
  'LIBRA': '♎',
  'SCORPIO': '♏',
  'SAGITTARIUS': '♐',
  'CAPRICORN': '♑',
  'AQUARIUS': '♒',
  'PISCES': '♓'
};

// 星座の色のマッピング（星座ごとの特色）
const zodiacColors: { [key: string]: string } = {
  'ARIES': 'from-red-600 to-orange-600',
  'TAURUS': 'from-green-600 to-emerald-600',
  'GEMINI': 'from-yellow-500 to-amber-500',
  'CANCER': 'from-blue-400 to-cyan-400',
  'LEO': 'from-orange-500 to-yellow-500',
  'VIRGO': 'from-indigo-600 to-blue-600',
  'LIBRA': 'from-pink-500 to-rose-500',
  'SCORPIO': 'from-purple-700 to-pink-700',
  'SAGITTARIUS': 'from-purple-600 to-indigo-600',
  'CAPRICORN': 'from-gray-600 to-slate-600',
  'AQUARIUS': 'from-cyan-500 to-blue-500',
  'PISCES': 'from-teal-500 to-cyan-500'
};

export default function Home() {
  const horoscopes = getDailyHoroscope();
  const today = new Date();
  const todayStr = today.toLocaleDateString('ja-JP', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  });

  return (
    <div className="min-h-screen relative bg-gradient-to-b from-slate-950 via-blue-950 to-indigo-950">
      <StarryBackground />
      
      {/* 星雲効果 */}
      <div className="fixed inset-0 opacity-30">
        <div className="absolute top-20 left-10 w-96 h-96 bg-purple-600 rounded-full filter blur-[120px]"></div>
        <div className="absolute bottom-20 right-10 w-96 h-96 bg-blue-600 rounded-full filter blur-[120px]"></div>
        <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-indigo-600 rounded-full filter blur-[120px]"></div>
      </div>

      <div className="container max-w-7xl mx-auto px-6 py-12 relative z-10">
        {/* ヘッダー */}
        <div className="text-center mb-12">
          <h1 className="text-6xl font-bold mb-4 bg-gradient-to-r from-blue-200 via-purple-200 to-pink-200 bg-clip-text text-transparent">
            星座占い
          </h1>
          <p className="text-2xl text-blue-100 opacity-80">
            {todayStr}
          </p>
          <div className="mt-4 flex justify-center gap-2">
            <span className="inline-block w-2 h-2 bg-white rounded-full animate-pulse"></span>
            <span className="inline-block w-2 h-2 bg-white rounded-full animate-pulse delay-100"></span>
            <span className="inline-block w-2 h-2 bg-white rounded-full animate-pulse delay-200"></span>
          </div>
        </div>
        
        {/* 4行3列のグリッド */}
        <div className="grid grid-cols-3 gap-8">
          {horoscopes
            .sort((a, b) => {
              // 星座の順番で並べる（牡羊座から魚座まで）
              const order = ['ARIES', 'TAURUS', 'GEMINI', 'CANCER', 'LEO', 'VIRGO', 
                           'LIBRA', 'SCORPIO', 'SAGITTARIUS', 'CAPRICORN', 'AQUARIUS', 'PISCES'];
              return order.indexOf(a.zodiac.name) - order.indexOf(b.zodiac.name);
            })
            .map((horoscope) => (
            <Link
              key={horoscope.zodiac.name}
              href={`/detail/${horoscope.zodiac.name}`}
              className="block transform transition-all duration-300 hover:scale-105"
            >
              <div className="relative h-full bg-gradient-to-br from-slate-900/50 to-slate-800/50 backdrop-blur-sm rounded-2xl border border-white/10 overflow-hidden group">
                {/* 背景のグラデーション */}
                <div className={`absolute inset-0 bg-gradient-to-br ${zodiacColors[horoscope.zodiac.name]} opacity-10 group-hover:opacity-20 transition-opacity duration-300`}></div>
                
                {/* 星座シンボル（大きく中央に） */}
                <div className="absolute inset-0 flex items-center justify-center">
                  <span className={`text-8xl opacity-10 group-hover:opacity-20 transition-opacity duration-300 bg-gradient-to-br ${zodiacColors[horoscope.zodiac.name]} bg-clip-text text-transparent`}>
                    {zodiacSymbols[horoscope.zodiac.name]}
                  </span>
                </div>

                {/* コンテンツ */}
                <div className="relative z-10 p-6">
                  {/* ランキングバッジ */}
                  <div className="absolute top-4 right-4">
                    <div className={`w-12 h-12 rounded-full flex items-center justify-center text-sm font-bold
                      ${horoscope.ranking <= 3 ? 'bg-gradient-to-br from-yellow-400 to-amber-500 text-slate-900' : 
                        horoscope.ranking <= 6 ? 'bg-gradient-to-br from-slate-400 to-slate-500 text-white' :
                        horoscope.ranking <= 9 ? 'bg-gradient-to-br from-amber-700 to-amber-800 text-white' :
                        'bg-gradient-to-br from-slate-600 to-slate-700 text-white'}
                      shadow-lg`}>
                      {horoscope.ranking}
                    </div>
                  </div>

                  {/* 星座名 */}
                  <div className="mb-4">
                    <h2 className="text-2xl font-bold text-white mb-1">
                      {horoscope.zodiac.kanjiName}
                    </h2>
                    <p className="text-sm text-blue-200 opacity-80">
                      {horoscope.zodiac.hiraganaName}
                    </p>
                  </div>

                  {/* 運勢（短縮版） */}
                  <p className="text-sm text-gray-200 line-clamp-2 mb-4">
                    {horoscope.fortune}
                  </p>

                  {/* ラッキーアイテム */}
                  <div className="flex items-center gap-2 text-xs">
                    <span className="text-yellow-300">✨</span>
                    <span className="text-gray-300">{horoscope.luckyItem}</span>
                  </div>
                </div>

                {/* ホバー時の光彩効果 */}
                <div className="absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none">
                  <div className="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-transparent via-white/30 to-transparent"></div>
                  <div className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-transparent via-white/30 to-transparent"></div>
                  <div className="absolute top-0 left-0 w-1 h-full bg-gradient-to-b from-transparent via-white/30 to-transparent"></div>
                  <div className="absolute top-0 right-0 w-1 h-full bg-gradient-to-b from-transparent via-white/30 to-transparent"></div>
                </div>
              </div>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}
