"use client";

import React from "react";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();

  return (
    <div 
      style={{ 
        width: "100vw",
        height: "100vh",
        display: "flex",
        justifyContent: "center", // จัดตรงกลางแนวนอน
        alignItems: "center", // จัดตรงกลางแนวตั้ง
        flexDirection: "column", // จัดเรียงปุ่มในแนวตั้ง
        backgroundImage: "url(/image/Desktop_home.jpg)",
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      {/* ปุ่มต่างๆ */}
      <img
        src="/image/buttom_play.png"
        alt="Play Button"
        style={{
          width: "20vw", // ใช้เป็น % ของ viewport width
          maxWidth: "300px", // จำกัดขนาดสูงสุด
          height: "auto",
          cursor: "pointer",
          transition: "0.3s",
          marginTop: "150px",
          marginBottom: "20px", // ระยะห่างระหว่างปุ่ม
        }}
        onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
        onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
        onClick={() => router.push("/Mode")}
      />

      <img
        src="/image/buttom_how_to_play.png"
        alt="How to Play Button"
        style={{
          width: "20vw",
          maxWidth: "300px",
          height: "auto",
          cursor: "pointer",
          transition: "0.3s",
        }}
        onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
        onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
        onClick={() => router.push("/howtoplay")}
      />
    </div>
  );
}
