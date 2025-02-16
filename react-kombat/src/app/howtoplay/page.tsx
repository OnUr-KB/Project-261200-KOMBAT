"use client";

import React from "react";
import { useRouter } from "next/navigation";

export default function Howtoplay() {
  const router = useRouter();

  return (
    <div
      style={{
        width: "100vw", 
        height: "100vh",
        display: "flex",
        position: "relative",
        justifyContent: "center",
        alignItems: "center",
        backgroundImage: "url('/image/Desktop_how_to_play.jpg')",
        backgroundSize: "contain", 
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat", 
      }}
    >
         {/* ปุ่ม Return */}
         <div
        style={{
          position: "absolute",
          top: "7%", 
          left: "15%", 
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          transform: "translate(-50%, -50%)", 
        }}
      >
        <img
          src="/image/return_button.png"
          alt="Return Button"
          style={{
            width: "4vw", 
            maxWidth: "70px",
            height: "auto",
            cursor: "pointer",
            transition: "opacity 0.3s",
          }}
          onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
          onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
          onClick={() => router.push("/")}
        />
      </div>
    </div>
  );
}
