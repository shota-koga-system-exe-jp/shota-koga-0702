import { Zodiac, HoroscopeResult, zodiacData } from '@/types/horoscope';

const FORTUNES = [
  "素晴らしい一日になるでしょう！新しいチャンスが訪れ、あなたの才能が輝きます。",
  "今日は絶好調！何をやってもうまくいく日です。積極的に行動しましょう。",
  "良い出会いがありそうです。人との繋がりを大切にすることで、幸運が舞い込みます。",
  "努力が実を結ぶ日です。これまでの頑張りが認められるでしょう。",
  "穏やかで充実した一日になります。心に余裕を持って過ごしましょう。",
  "小さな幸せがたくさん見つかる日です。日常の中に喜びを見出しましょう。",
  "バランスの取れた一日です。仕事もプライベートも順調に進みます。",
  "新しいアイデアが浮かびやすい日です。創造力を活かしてみましょう。",
  "コミュニケーションが円滑に進む日です。大切な話し合いには最適です。",
  "少し慎重に行動した方が良い日です。焦らずゆっくり進みましょう。",
  "体調管理に気をつけましょう。無理をせず、休息を大切にしてください。",
  "今日は充電の日です。明日への準備をしっかりと整えましょう。"
];

const LUCKY_ITEMS = [
  "青いペン", "白いハンカチ", "観葉植物", "コーヒー", "音楽プレーヤー",
  "本", "時計", "香水", "チョコレート", "スマートフォン",
  "財布", "鍵", "メガネ", "ノート", "傘",
  "マスク", "ミントガム", "水筒", "イヤホン", "手帳",
  "ティッシュ", "リップクリーム", "ハンドクリーム", "カメラ", "お守り"
];

const LUCKY_COLORS = [
  "赤", "青", "黄色", "緑", "紫", "オレンジ", "ピンク", "白", "黒", "茶色", "金色", "銀色"
];

function seededRandom(seed: number): () => number {
  return function() {
    seed = (seed * 9301 + 49297) % 233280;
    return seed / 233280;
  };
}

export function getDailyHoroscope(): HoroscopeResult[] {
  const today = new Date();
  const seed = today.getFullYear() * 10000 + (today.getMonth() + 1) * 100 + today.getDate();
  const random = seededRandom(seed);
  
  const results: HoroscopeResult[] = [];
  const zodiacs = Object.values(Zodiac);
  
  // シャッフルして順位を決定
  const shuffled = [...zodiacs].sort(() => random() - 0.5);
  
  for (let i = 0; i < shuffled.length; i++) {
    const zodiacName = shuffled[i];
    const zodiacInfo = zodiacData[zodiacName];
    const ranking = i + 1;
    
    // ランキングに基づいて運勢を選択
    const fortune = FORTUNES[i];
    
    // ランダムにラッキーアイテムを選択
    const luckyItem = LUCKY_ITEMS[Math.floor(random() * LUCKY_ITEMS.length)];
    
    // ラッキーナンバー（1-9）
    const luckyNumber = Math.floor(random() * 9) + 1;
    
    // ラッキーカラー
    const luckyColor = LUCKY_COLORS[Math.floor(random() * LUCKY_COLORS.length)];
    
    results.push({
      zodiac: zodiacInfo,
      ranking,
      fortune,
      luckyItem,
      luckyNumber,
      luckyColor
    });
  }
  
  // ランキング順にソート
  return results.sort((a, b) => a.ranking - b.ranking);
}

export function getHoroscopeByZodiac(zodiac: Zodiac): HoroscopeResult | null {
  const allResults = getDailyHoroscope();
  return allResults.find(result => result.zodiac.name === zodiac) || null;
}

export function getZodiacDateRange(zodiac: Zodiac): string {
  const info = zodiacData[zodiac];
  const startMonth = info.startMonth + "月";
  const startDay = info.startDay + "日";
  const endMonth = info.endMonth + "月";
  const endDay = info.endDay + "日";
  
  // 年をまたぐ星座（山羊座）の場合の特別な処理
  if (zodiac === Zodiac.CAPRICORN) {
    return "12月22日 ～ 1月19日生まれ";
  }
  
  return startMonth + startDay + " ～ " + endMonth + endDay + "生まれ";
}

export function generateLuckyTime(ranking: number): string {
  const times = [
    "7:00～9:00", "9:00～11:00", "11:00～13:00", 
    "13:00～15:00", "15:00～17:00", "17:00～19:00",
    "19:00～21:00", "21:00～23:00", "10:00～12:00",
    "14:00～16:00", "16:00～18:00", "20:00～22:00"
  ];
  
  return times[ranking - 1];
}

export function generateStars(ranking: number, category: string): string {
  let stars: number;
  switch (category) {
    case "love":
      stars = ranking <= 4 ? 5 : ranking <= 8 ? 3 : 2;
      break;
    case "work":
      stars = ranking <= 3 ? 5 : ranking <= 7 ? 4 : 2;
      break;
    case "money":
      stars = ranking <= 5 ? 4 : ranking <= 9 ? 3 : 2;
      break;
    case "health":
      stars = ranking <= 6 ? 4 : ranking <= 10 ? 3 : 2;
      break;
    default:
      stars = 3;
  }
  
  let result = "";
  for (let i = 0; i < 5; i++) {
    if (i < stars) {
      result += "★";
    } else {
      result += "☆";
    }
  }
  return result;
}

export function generateAdvice(ranking: number): string {
  if (ranking <= 3) {
    return "今日は絶好調の一日です！積極的に行動することで、素晴らしい結果が得られるでしょう。新しいことにチャレンジするのに最適な日です。周りの人にも幸運を分けてあげましょう。";
  } else if (ranking <= 6) {
    return "安定した良い運気の日です。普段通りの行動を心がけながら、少し冒険してみるのも良いでしょう。人間関係を大切にすることで、さらに運気が上昇します。";
  } else if (ranking <= 9) {
    return "今日は慎重に行動することが大切です。大きな決断は避け、じっくりと考える時間を持ちましょう。小さな幸せを見つけることで、気持ちが前向きになります。";
  } else {
    return "今日は充電の日と考えましょう。無理をせず、リラックスして過ごすことが大切です。明日への準備期間と捉え、心身を整えることに集中しましょう。";
  }
}