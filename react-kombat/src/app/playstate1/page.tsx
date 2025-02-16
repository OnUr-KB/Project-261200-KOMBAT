// pages/yourPage.tsx
"use client"
import React from "react";
import { useRouter } from "next/navigation";
import { Container, NumberInput } from "@mantine/core";
import Navbar from "../components/navbar1"; // สำหรับไฟล์ที่อยู่ในโฟลเดอร์เดียวกันกับหน้า
 

const YourPage = () => {
  const router = useRouter();

  return (
    <div style={{ position: "relative" }}>
      {/* พื้นหลัง */}
      <div
        style={{
          width: "100%",
          height: "100vh",
          position: "fixed",
          top: 0,
          left: 0,
          backgroundImage: "url()",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          zIndex: -1,
        }}
      />
      
      {/* ใช้ Navbar Component */}
      <Navbar />

      {/* เนื้อหาหน้า */}
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
          margin: 0,
        }}
      >
        

        {/* ปุ่ม */}
        <img
          src="/image/return_button.png"
          alt="Return Button"
          style={{
            position: "absolute",
            top: "85%",
            left: "5%",
            width: "50px",
            height: "50px",
            cursor: "pointer",
            transition: "opacity 0.3s",
            zIndex: 10,
          }}
          onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
          onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
          onClick={() => router.push("/Mode")}
        />

        {/* ปุ่มอื่นๆ */}
        <img
          src="/image/button_home.png"
          alt="Home Button"
          style={{
            position: "absolute",
            top: "85%",
            left: "14%",
            width: "50px",
            height: "50px",
            cursor: "pointer",
            transition: "opacity 0.3s",
            zIndex: 10,
          }}
          onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
          onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
          onClick={() => router.push("/")}
        />

        <img
          src="/image/button_Next_.png"
          alt="Play Button"
          style={{
            width: "20vw",
            maxWidth: "300px",
            position: "absolute",
            top: "85%",
            left: "75%",
            height: "auto",
            cursor: "pointer",
            transition: "0.3s",
          }}
          onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
          onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
          onClick={() => router.push("/playstate1")}
        />
      </div>
    </div>
  );
};

export default YourPage;
