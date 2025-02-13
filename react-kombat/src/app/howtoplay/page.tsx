"use client";

import React from "react";
import { useRouter } from "next/navigation";

export default function Howtoplay() {
  const router = useRouter();

  return (
    <div
      style={{
        width: "100vw", // เต็มจอแนวนอน
        height: "100vh", // เต็มจอแนวตั้ง
        position: "relative",
        backgroundImage: "url('/image/Desktop_how_to_play.jpg')", // ตรวจสอบ path
        backgroundSize: "contain",
        backgroundPosition: "center",
          backgroundRepeat: "no-repeat"
      }}
    >
      {/* ปุ่ม Return */}
      <img
        src="/image/return_button.png"
        alt="Return Button"
        style={{
          position: "absolute",
          top: "5%", // ปรับให้อยู่ด้านบนสุด
          left: "18%", // ปรับให้อยู่ด้านซ้าย
          width: "50px", // ปรับขนาดให้เล็กลง
          height: "50px",
          cursor: "pointer",
          transition: "opacity 0.3s",
          zIndex: 10, // ทำให้แสดงอยู่ด้านบน
        }}
        onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
        onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
        onClick={() => router.push("/Home")}
      />
    </div>
  );
}
