"use client";

import React from "react";
import { useRouter } from "next/navigation";

export default function Mode() {
  const router = useRouter();
  return (
    <div 
      style={{ 
        width: "100vw",
        height: "100vh",
        position: "relative",
        backgroundImage: "url(/image/Desktop_select_mode.jpg)", // ไม่มี . นำหน้า
        backgroundSize: "cover",
        backgroundPosition: "center",
      //  backgroundRepeat: "no-repeat",
        display: "flex",
        justifyContent: "center", // จัดตรงกลางแนวนอน
        alignItems: "center", // จัดตรงกลางแนวตั้ง
        flexDirection: "column", // จัดเรียงปุ่มในแนวตั้ง
      }}
    >
      <div className="d-flex justify-content-center align-items-center vh-100 gap-5" >
  <img src="/image/2player.png" className="rounded img-fluid" alt="mode1" 
    style={{ maxWidth: "200px",marginBottom: "-20vh" }} 
      onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.9")}
      onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
      onClick={() => router.push("/selectplayer")}  />
  <img src="/image/player_bot.png" className="rounded img-fluid" alt="mode2" 
    style={{ maxWidth: "200px",marginBottom: "-20vh"  }} 
      onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.9")}
      onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
      onClick={() => router.push("/player1")}  />

  <img src="/image/2bot.png" className="rounded img-fluid" alt="mode3" 
  style={{ maxWidth: "200px",marginBottom: "-20vh"  }} 
  onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.9")}
  onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
  onClick={() => router.push("/player1")}
  />
  </div>
<div 
  style={{
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    marginBottom: "15vh", 
    // ปรับระยะห่างระหว่างรูปภาพกับปุ่ม
  }}
>


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
 {/* ปุ่ม Return */}
 <img
        src="/image/return_button.png"
        alt="Return Button"
        style={{
          position: "absolute",
          top: "5%", // ปรับให้อยู่ด้านบนสุด
          left: "10%", // ปรับให้อยู่ด้านซ้าย
          width: "70px", // ปรับขนาดให้เล็กลง
          height: "70px",
          cursor: "pointer",
          transition: "opacity 0.3s",
          zIndex: 10, // ทำให้แสดงอยู่ด้านบน
        }}
        onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
        onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
        onClick={() => router.push("/")}
      />

      
      
    </div>
  )
}