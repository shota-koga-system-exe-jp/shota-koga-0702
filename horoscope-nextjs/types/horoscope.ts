export enum Zodiac {
  ARIES = 'ARIES',
  TAURUS = 'TAURUS',
  GEMINI = 'GEMINI',
  CANCER = 'CANCER',
  LEO = 'LEO',
  VIRGO = 'VIRGO',
  LIBRA = 'LIBRA',
  SCORPIO = 'SCORPIO',
  SAGITTARIUS = 'SAGITTARIUS',
  CAPRICORN = 'CAPRICORN',
  AQUARIUS = 'AQUARIUS',
  PISCES = 'PISCES'
}

export interface ZodiacInfo {
  name: Zodiac;
  kanjiName: string;
  hiraganaName: string;
  symbol: string;
  startMonth: number;
  startDay: number;
  endMonth: number;
  endDay: number;
}

export interface HoroscopeResult {
  zodiac: ZodiacInfo;
  ranking: number;
  fortune: string;
  luckyItem: string;
  luckyNumber: number;
  luckyColor: string;
}

export const zodiacData: Record<Zodiac, ZodiacInfo> = {
  [Zodiac.ARIES]: {
    name: Zodiac.ARIES,
    kanjiName: '牡羊座',
    hiraganaName: 'おひつじ座',
    symbol: '♈',
    startMonth: 3,
    startDay: 21,
    endMonth: 4,
    endDay: 19
  },
  [Zodiac.TAURUS]: {
    name: Zodiac.TAURUS,
    kanjiName: '牡牛座',
    hiraganaName: 'おうし座',
    symbol: '♉',
    startMonth: 4,
    startDay: 20,
    endMonth: 5,
    endDay: 20
  },
  [Zodiac.GEMINI]: {
    name: Zodiac.GEMINI,
    kanjiName: '双子座',
    hiraganaName: 'ふたご座',
    symbol: '♊',
    startMonth: 5,
    startDay: 21,
    endMonth: 6,
    endDay: 21
  },
  [Zodiac.CANCER]: {
    name: Zodiac.CANCER,
    kanjiName: '蟹座',
    hiraganaName: 'かに座',
    symbol: '♋',
    startMonth: 6,
    startDay: 22,
    endMonth: 7,
    endDay: 22
  },
  [Zodiac.LEO]: {
    name: Zodiac.LEO,
    kanjiName: '獅子座',
    hiraganaName: 'しし座',
    symbol: '♌',
    startMonth: 7,
    startDay: 23,
    endMonth: 8,
    endDay: 22
  },
  [Zodiac.VIRGO]: {
    name: Zodiac.VIRGO,
    kanjiName: '乙女座',
    hiraganaName: 'おとめ座',
    symbol: '♍',
    startMonth: 8,
    startDay: 23,
    endMonth: 9,
    endDay: 22
  },
  [Zodiac.LIBRA]: {
    name: Zodiac.LIBRA,
    kanjiName: '天秤座',
    hiraganaName: 'てんびん座',
    symbol: '♎',
    startMonth: 9,
    startDay: 23,
    endMonth: 10,
    endDay: 23
  },
  [Zodiac.SCORPIO]: {
    name: Zodiac.SCORPIO,
    kanjiName: '蠍座',
    hiraganaName: 'さそり座',
    symbol: '♏',
    startMonth: 10,
    startDay: 24,
    endMonth: 11,
    endDay: 22
  },
  [Zodiac.SAGITTARIUS]: {
    name: Zodiac.SAGITTARIUS,
    kanjiName: '射手座',
    hiraganaName: 'いて座',
    symbol: '♐',
    startMonth: 11,
    startDay: 23,
    endMonth: 12,
    endDay: 21
  },
  [Zodiac.CAPRICORN]: {
    name: Zodiac.CAPRICORN,
    kanjiName: '山羊座',
    hiraganaName: 'やぎ座',
    symbol: '♑',
    startMonth: 12,
    startDay: 22,
    endMonth: 1,
    endDay: 19
  },
  [Zodiac.AQUARIUS]: {
    name: Zodiac.AQUARIUS,
    kanjiName: '水瓶座',
    hiraganaName: 'みずがめ座',
    symbol: '♒',
    startMonth: 1,
    startDay: 20,
    endMonth: 2,
    endDay: 18
  },
  [Zodiac.PISCES]: {
    name: Zodiac.PISCES,
    kanjiName: '魚座',
    hiraganaName: 'うお座',
    symbol: '♓',
    startMonth: 2,
    startDay: 19,
    endMonth: 3,
    endDay: 20
  }
};