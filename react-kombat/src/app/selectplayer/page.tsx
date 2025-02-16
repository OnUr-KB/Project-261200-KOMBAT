"use client";

import React from "react";
import { useRouter } from "next/navigation";


export default function selectplayer() {
    const router = useRouter();
  return (
    <div style={{ 
        width: "100vw",
        height: "100vh",
        position: "relative",
        backgroundImage: "url(/image/Destop_player_select.jpg)",
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat"
      }}>

 {/* ปุ่ม Return */}
<img
        src="/image/return_button.png"
        alt="Return Button"
        style={{
          position: "absolute",
          top: "85%", // ปรับให้อยู่ด้านบนสุด
          left: "5%", // ปรับให้อยู่ด้านซ้าย
          width: "50px", // ปรับขนาดให้เล็กลง
          height: "50px",
          cursor: "pointer",
          transition: "opacity 0.3s",
          zIndex: 10, // ทำให้แสดงอยู่ด้านบน
        }}
        onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
        onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
        onClick={() => router.push("/Mode")}
      />
       {/* ปุ่ม home */}
<img
        src="/image/button_home.png"
        alt="home Button"
        style={{
          position: "absolute",
          top: "85%", // ปรับให้อยู่ด้านบนสุด
          left: "12%", // ปรับให้อยู่ด้านซ้าย
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


<div>
<img 
      src="/image/player1.png" 
      className="rounded img-fluid" 
      alt="mode1" 
      style={{ maxWidth: "200px",
        position: "absolute",
        top: "70%", // ปรับให้อยู่ด้านบนสุด
        left: "20%",
         cursor: "pointer" }} 
      onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.9")}
      onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
      onClick={() => router.push("/player1")}  
    />

    <img 
      src="/image/player2.png" 
      className="rounded img-fluid" 
      alt="mode3" 
      style={{ maxWidth: "200px",
        position: "absolute",
        top: "70%", // ปรับให้อยู่ด้านบนสุด
        left: "60%", 
         cursor: "pointer"

       }} 
      onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.9")}
      onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
      onClick={() => router.push("/player2")}  
    />

   
  
</div>

      </div>

      
  )
}
