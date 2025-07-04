'use client';

import { useEffect, useState } from 'react';

interface Star {
  id: number;
  x: number;
  y: number;
  size: number;
  type: 'white' | 'yellow' | 'blue' | 'red';
  animationDelay: number;
  brightness: number;
}

interface ShootingStar {
  id: number;
  x: number;
  y: number;
  duration: number;
}

export default function StarryBackground() {
  const [stars, setStars] = useState<Star[]>([]);
  const [shootingStars, setShootingStars] = useState<ShootingStar[]>([]);

  useEffect(() => {
    // 星を生成
    const newStars: Star[] = [];
    const starCount = 200; // 星の数を増やす

    for (let i = 0; i < starCount; i++) {
      const starType = Math.random();
      let type: 'white' | 'yellow' | 'blue' | 'red' = 'white';
      
      if (starType > 0.9) type = 'yellow';
      else if (starType > 0.8) type = 'blue';
      else if (starType > 0.7) type = 'red';

      newStars.push({
        id: i,
        x: Math.random() * 100,
        y: Math.random() * 100,
        size: Math.random() * 3 + 1,
        type,
        animationDelay: Math.random() * 5,
        brightness: Math.random() * 0.5 + 0.5
      });
    }
    setStars(newStars);

    // 流れ星を定期的に生成
    const shootingStarInterval = setInterval(() => {
      if (Math.random() > 0.7) {
        const newShootingStar: ShootingStar = {
          id: Date.now(),
          x: Math.random() * 100,
          y: Math.random() * 30,
          duration: Math.random() * 1 + 1
        };
        
        setShootingStars(prev => [...prev, newShootingStar]);
        
        // アニメーション後に削除
        setTimeout(() => {
          setShootingStars(prev => prev.filter(star => star.id !== newShootingStar.id));
        }, (newShootingStar.duration + 0.5) * 1000);
      }
    }, 2000);

    return () => clearInterval(shootingStarInterval);
  }, []);

  return (
    <div className="fixed inset-0 overflow-hidden pointer-events-none">
      {/* 天の川 */}
      <div className="milky-way"></div>
      
      {/* 星雲 */}
      <div className="nebula w-[600px] h-[600px] bg-purple-600/20 top-[10%] left-[20%]"></div>
      <div className="nebula w-[400px] h-[400px] bg-blue-600/20 bottom-[20%] right-[10%]"></div>
      <div className="nebula w-[500px] h-[500px] bg-indigo-600/20 top-[50%] left-[60%]"></div>
      
      {/* 星 */}
      {stars.map(star => (
        <div
          key={star.id}
          className={`star ${star.type} ${star.size > 2 ? 'bright' : ''}`}
          style={{
            left: `${star.x}%`,
            top: `${star.y}%`,
            width: `${star.size}px`,
            height: `${star.size}px`,
            animationDelay: `${star.animationDelay}s`,
            opacity: star.brightness
          }}
        />
      ))}
      
      {/* 流れ星 */}
      {shootingStars.map(star => (
        <div
          key={star.id}
          className="shooting-star"
          style={{
            left: `${star.x}%`,
            top: `${star.y}%`,
            animationDuration: `${star.duration}s`
          }}
        />
      ))}
      
      {/* 追加の装飾的な要素 */}
      <div className="fixed top-0 left-0 w-full h-full">
        {/* グラデーションオーバーレイ */}
        <div className="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent"></div>
        
        {/* 星座の線（装飾的） */}
        <svg className="absolute inset-0 w-full h-full opacity-5">
          <line x1="10%" y1="20%" x2="25%" y2="35%" stroke="white" strokeWidth="1" />
          <line x1="25%" y1="35%" x2="30%" y2="25%" stroke="white" strokeWidth="1" />
          <line x1="70%" y1="60%" x2="85%" y2="70%" stroke="white" strokeWidth="1" />
          <line x1="85%" y1="70%" x2="90%" y2="65%" stroke="white" strokeWidth="1" />
          <circle cx="10%" cy="20%" r="2" fill="white" />
          <circle cx="25%" cy="35%" r="2" fill="white" />
          <circle cx="30%" cy="25%" r="2" fill="white" />
          <circle cx="70%" cy="60%" r="2" fill="white" />
          <circle cx="85%" cy="70%" r="2" fill="white" />
          <circle cx="90%" cy="65%" r="2" fill="white" />
        </svg>
      </div>
    </div>
  );
}