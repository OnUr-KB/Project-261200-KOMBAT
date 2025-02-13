"use client";

import React from "react";
import { useRouter } from "next/navigation";

export default function mode
() {
  const router = useRouter();
  return (
    <div 
      style={{ 
        width: "100vw",
        height: "100vh",
        position: "relative",
        backgroundImage: "url(/image/Desktop_mode.jpg)", // ไม่มี `.` นำหน้า
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      {/* ปุ่ม Bootstrap */}
      <div className="d-grid gap-2 col-6 mx-auto" style={{ position: "relative", zIndex: 1 }}>
  <img
    src="/image/buttom_play.png"
    alt="Button 1"
    style={{
      position: "absolute",
      top: "375px",
      left : "270px",
      width: "320px",
      height: "100px",
      cursor: "pointer",
      transition: "0.3s",
    }}
    onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
    onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
    onClick={() => router.push("/Mode")} 
  />
  <img
    src="/image/buttom_how_to_play.png"
    alt="Button 2"
    style={{
      position : "absolute",
      top: "486px",
      left: "270px",
      width: "320px",
      height: "100px",
      cursor: "pointer",
      transition: "0.3s",
    }}
    onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
    onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
    onClick={() => router.push("/howtoplay")} // เปลี่ยนไปหน้า /how-to-play
  />
</div>

    </div>
  )
}
