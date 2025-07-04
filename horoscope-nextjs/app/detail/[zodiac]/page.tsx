'use client';

import { useState } from 'react';
import { useParams } from 'next/navigation';
import Link from 'next/link';
import { Zodiac } from '@/types/horoscope';
import { 
  getHoroscopeByZodiac, 
  getZodiacDateRange, 
  generateLuckyTime, 
  generateStars,
  generateAdvice 
} from '@/lib/horoscope-service';

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

// 星座の色のマッピング
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

interface FortuneDetail {
  good: string;
  normal: string;
  bad: string;
}

interface FortuneDetails {
  [key: string]: FortuneDetail;
}

const fortunes: FortuneDetails = {
  love: {
    good: '恋愛運は絶好調！素敵な出会いや、パートナーとの関係が深まる暗示があります。積極的にコミュニケーションを取ることで、より良い関係を築けるでしょう。',
    normal: '恋愛運は安定しています。焦らず自然体でいることが大切です。相手の気持ちを考えながら行動すると良いでしょう。',
    bad: '恋愛運は少し低調です。今は自分磨きに専念する時期かもしれません。焦らず、タイミングを待ちましょう。'
  },
  work: {
    good: '仕事運は最高潮！新しいプロジェクトや昇進のチャンスが訪れそうです。積極的に挑戦することで、大きな成果を得られるでしょう。',
    normal: '仕事運は順調です。コツコツと努力を続けることで、着実に成果を上げられます。チームワークを大切にしましょう。',
    bad: '仕事運は少し注意が必要です。ミスを防ぐため、いつも以上に慎重に行動しましょう。休息も大切です。'
  },
  money: {
    good: '金運は絶好調！臨時収入や投資のチャンスがありそうです。ただし、使いすぎには注意しましょう。',
    normal: '金運は安定しています。計画的な支出を心がけることで、着実に貯蓄を増やせるでしょう。',
    bad: '金運は少し低調です。衝動買いは避け、必要なものだけに支出を絞りましょう。'
  },
  health: {
    good: '健康運は絶好調！エネルギーに満ち溢れています。新しい運動や健康法を始めるのに最適な時期です。',
    normal: '健康運は安定しています。規則正しい生活を心がけることで、良い状態を維持できるでしょう。',
    bad: '健康運は少し注意が必要です。無理をせず、十分な休息を取ることを心がけましょう。'
  }
};

const categoryIcons: { [key: string]: string } = {
  love: '💝',
  work: '💼',
  money: '💰',
  health: '🌟'
};

const categoryTitles: { [key: string]: string } = {
  love: '恋愛運',
  work: '仕事運',
  money: '金運',
  health: '健康運'
};

export default function DetailPage() {
  const params = useParams();
  const zodiacName = params.zodiac as string;
  const [modalOpen, setModalOpen] = useState(false);
  const [modalCategory, setModalCategory] = useState('');
  
  const horoscope = getHoroscopeByZodiac(zodiacName as Zodiac);
  
  if (!horoscope) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-slate-950 via-blue-950 to-indigo-950 flex items-center justify-center">
        <div className="text-center">
          <h1 className="text-3xl text-white mb-4">エラーが発生しました</h1>
          <Link href="/" className="cosmic-button inline-block">
            トップページに戻る
          </Link>
        </div>
      </div>
    );
  }

  const dateRange = getZodiacDateRange(horoscope.zodiac.name);
  const luckyTime = generateLuckyTime(horoscope.ranking);
  const advice = generateAdvice(horoscope.ranking);

  const showModal = (category: string) => {
    setModalCategory(category);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  const getFortuneLevel = (ranking: number) => {
    if (ranking <= 4) return 'good';
    if (ranking <= 8) return 'normal';
    return 'bad';
  };

  const getFortunePercentage = (ranking: number) => {
    return Math.round((13 - ranking) / 12 * 100);
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-blue-950 to-indigo-950">
      {/* 星雲効果 */}
      <div className="fixed inset-0 opacity-30">
        <div className="absolute top-20 left-10 w-96 h-96 bg-purple-600 rounded-full filter blur-[120px]"></div>
        <div className="absolute bottom-20 right-10 w-96 h-96 bg-blue-600 rounded-full filter blur-[120px]"></div>
      </div>

      <div className="container max-w-5xl mx-auto px-6 py-12 relative z-10">
        <Link href="/" className="inline-flex items-center gap-2 text-blue-200 hover:text-white transition-colors mb-8">
          <span>←</span>
          <span>星座一覧に戻る</span>
        </Link>
        
        <div className="detail-card rounded-3xl p-8 relative overflow-hidden">
          {/* 背景の星座シンボル */}
          <div className="absolute inset-0 flex items-center justify-center opacity-5">
            <span className={`text-[300px] bg-gradient-to-br ${zodiacColors[horoscope.zodiac.name]} bg-clip-text text-transparent`}>
              {zodiacSymbols[horoscope.zodiac.name]}
            </span>
          </div>

          {/* ヘッダー */}
          <div className="text-center mb-10 relative z-10">
            <div className={`inline-block text-8xl mb-4 bg-gradient-to-br ${zodiacColors[horoscope.zodiac.name]} bg-clip-text text-transparent zodiac-symbol-glow`}>
              {zodiacSymbols[horoscope.zodiac.name]}
            </div>
            <h1 className="text-5xl font-bold text-white mb-2">
              {horoscope.zodiac.kanjiName}
            </h1>
            <p className="text-xl text-blue-200 mb-2">
              {horoscope.zodiac.hiraganaName}
            </p>
            <p className="text-gray-400 mb-4">
              {dateRange}
            </p>
            <div className="text-2xl text-gray-300">
              {new Date().toLocaleDateString('ja-JP', { 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric',
                weekday: 'long'
              })}
            </div>
          </div>

          {/* ランキングと運勢レベル */}
          <div className="mb-10 relative z-10">
            <div className="flex items-center justify-center gap-8 mb-6">
              <div className="text-center">
                <div className={`w-24 h-24 rounded-full flex items-center justify-center text-3xl font-bold
                  ${horoscope.ranking <= 3 ? 'bg-gradient-to-br from-yellow-400 to-amber-500 text-slate-900' : 
                    horoscope.ranking <= 6 ? 'bg-gradient-to-br from-slate-400 to-slate-500 text-white' :
                    horoscope.ranking <= 9 ? 'bg-gradient-to-br from-amber-700 to-amber-800 text-white' :
                    'bg-gradient-to-br from-slate-600 to-slate-700 text-white'}
                  shadow-2xl`}>
                  {horoscope.ranking}位
                </div>
              </div>
              <div className="flex-1 max-w-md">
                <div className="text-sm text-gray-400 mb-2">今日の運勢レベル</div>
                <div className="fortune-level">
                  <div 
                    className="fortune-level-bar" 
                    style={{ width: `${getFortunePercentage(horoscope.ranking)}%` }}
                  ></div>
                </div>
                <div className="text-right text-sm text-gray-400 mt-1">
                  {getFortunePercentage(horoscope.ranking)}%
                </div>
              </div>
            </div>
          </div>

          {/* 今日の運勢 */}
          <div className="mb-10 relative z-10">
            <h2 className="text-2xl font-bold text-white mb-4 flex items-center gap-2">
              <span>✨</span>
              <span>今日の運勢</span>
            </h2>
            <p className="text-lg leading-relaxed text-gray-200 bg-white/5 rounded-2xl p-6">
              {horoscope.fortune}
            </p>
          </div>

          {/* ラッキー情報 */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-10 relative z-10">
            <div className="bg-white/5 backdrop-blur rounded-2xl p-6 text-center border border-white/10">
              <div className="text-3xl mb-2">🎁</div>
              <div className="text-sm text-gray-400 mb-2">ラッキーアイテム</div>
              <div className="text-lg font-bold text-yellow-300">
                {horoscope.luckyItem}
              </div>
            </div>
            <div className="bg-white/5 backdrop-blur rounded-2xl p-6 text-center border border-white/10">
              <div className="text-3xl mb-2">🔢</div>
              <div className="text-sm text-gray-400 mb-2">ラッキーナンバー</div>
              <div className="text-lg font-bold text-yellow-300">
                {horoscope.luckyNumber}
              </div>
            </div>
            <div className="bg-white/5 backdrop-blur rounded-2xl p-6 text-center border border-white/10">
              <div className="text-3xl mb-2">🎨</div>
              <div className="text-sm text-gray-400 mb-2">ラッキーカラー</div>
              <div className="text-lg font-bold text-yellow-300">
                {horoscope.luckyColor}
              </div>
            </div>
            <div className="bg-white/5 backdrop-blur rounded-2xl p-6 text-center border border-white/10">
              <div className="text-3xl mb-2">⏰</div>
              <div className="text-sm text-gray-400 mb-2">ラッキータイム</div>
              <div className="text-lg font-bold text-yellow-300">
                {luckyTime}
              </div>
            </div>
          </div>

          {/* アドバイス */}
          <div className="mb-10 relative z-10">
            <div className={`bg-gradient-to-br ${zodiacColors[horoscope.zodiac.name]} p-[1px] rounded-2xl`}>
              <div className="bg-slate-900 rounded-2xl p-6">
                <h3 className="text-xl font-bold text-white mb-4 flex items-center gap-2">
                  <span>💫</span>
                  <span>今日のアドバイス</span>
                </h3>
                <p className="text-gray-200 leading-relaxed">
                  {advice}
                </p>
              </div>
            </div>
          </div>

          {/* 各分野の運勢 */}
          <div className="relative z-10">
            <h2 className="text-2xl font-bold text-white mb-6 text-center">
              各分野の詳細運勢
            </h2>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              {['love', 'work', 'money', 'health'].map((category) => (
                <button
                  key={category}
                  onClick={() => showModal(category)}
                  className="bg-white/5 backdrop-blur rounded-2xl p-6 text-center border border-white/10 
                           hover:bg-white/10 hover:border-white/20 transition-all duration-300 group"
                >
                  <div className="text-4xl mb-3 group-hover:scale-110 transition-transform">
                    {categoryIcons[category]}
                  </div>
                  <div className="text-sm text-gray-400 mb-2">{categoryTitles[category]}</div>
                  <div className="text-lg font-bold text-yellow-300">
                    {generateStars(horoscope.ranking, category)}
                  </div>
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* モーダル */}
      {modalOpen && (
        <div className="fixed inset-0 modal-backdrop z-50 flex items-center justify-center p-4" onClick={closeModal}>
          <div className="detail-card rounded-3xl p-8 max-w-2xl w-full transform scale-100 transition-transform" 
               onClick={(e) => e.stopPropagation()}>
            <button 
              onClick={closeModal} 
              className="float-right text-gray-400 hover:text-white text-3xl font-bold transition-colors"
            >
              ×
            </button>
            <div className="text-center mb-6">
              <div className="text-5xl mb-4">{categoryIcons[modalCategory]}</div>
              <h3 className="text-3xl font-bold text-white">
                {categoryTitles[modalCategory]}の詳細
              </h3>
            </div>
            <div className="space-y-4">
              <p className="text-gray-200 leading-relaxed bg-white/5 rounded-2xl p-6">
                {fortunes[modalCategory][getFortuneLevel(horoscope.ranking)]}
              </p>
              <div className={`bg-gradient-to-br ${zodiacColors[horoscope.zodiac.name]} p-[1px] rounded-2xl`}>
                <div className="bg-slate-900 rounded-2xl p-6">
                  <p className="text-blue-200 leading-relaxed">
                    💡 {fortunes[modalCategory][getFortuneLevel(horoscope.ranking)].split('。')[1]}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}