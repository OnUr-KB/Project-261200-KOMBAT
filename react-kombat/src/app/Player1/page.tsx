"use client"
import React, { useState, useEffect} from "react";
import { Container, Flex, NumberInput } from "@mantine/core";
import { useRouter } from "next/navigation";
import Navbar from "../components/navbar1"; 

export default function Page() {
  const [data, setData] = useState({ money: 0, hex: 0, minions: 0 });
  const router = useRouter();
  useEffect(() => {
    // เรียก API เพื่อดึงข้อมูลจาก backend
    fetch("https://api.example.com/user-stats") // เปลี่ยน URL ให้ตรงกับ backend จริง
      .then((response) => response.json())
      .then((result) => {
        setData({
          money: result.money,
          hex: result.hex,
          minions: result.minions,
        });
      })
      .catch((error) => console.error("Error fetching data:", error));
  }, []);


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
          backgroundImage: "url(/image/Desktopst1_p1.jpg)",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          zIndex: -1,
        }}
      />

      {/* Navbar */}
      <Navbar />

      {/* Input กลางหน้าจอ */}
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
          margin: 0,
        }}
      >
        <Container>
          <NumberInput size="lg" min={1} max={3} />
        </Container>
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
        onClick={() => router.push("/")}
      />
  {/* ปุ่ม next */}
  <img
        src="/image/button_Next_.png"
        alt="Play Button"
        style={{
          width: "20vw", // ใช้เป็น % ของ viewport width
          maxWidth: "300px", // จำกัดขนาดสูงสุด
          position: "absolute",
          top: "85%",
          left: "75%",
          height: "auto",
          cursor: "pointer",
          transition: "0.3s",
         // marginTop: "200px",
          //marginBottom: "2px", // ระยะห่างระหว่างปุ่ม
        }}
        onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
        onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
        onClick={() => router.push("/create_strategy")}
      />
      </div>
    </div>
  );
}
