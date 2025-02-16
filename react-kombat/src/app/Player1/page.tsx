"use client"
import React, { useState, useEffect} from "react";
import { Container, Flex, NumberInput } from "@mantine/core";
import { useRouter } from "next/navigation";


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

  const demoProps = {
    bg: "#6D2323",
    h: 60,
  };

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
      <Container
        size="xl"
        {...demoProps}
        style={{
          position: "fixed",
          zIndex: 1,
          maxWidth: "100%",
          display: "flex",
          width: "1036px",
          height: "70px",
         // justifyContent: "center",
          alignItems: "flex-start",
          gap: "41px",
          flexShrink: 0,
        }}
      >
        <Flex align="center" justify="flex-start" gap="md">
          <div style={{ position: "relative" }}>
            <div
              style={{
                width: 75,
                height: 75,
                borderRadius: "50%",
                border: "3px solid #6D2323",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                backgroundColor: "white",
              }}
            >
              <img
                src="/image/butter_bear.jpg"
                alt="Profile"
                style={{ width: 65, height: 65, borderRadius: "50%", objectFit: "cover" }}
              />
            </div>
            <span
              style={{
                position: "absolute",
                bottom: 5,
                right: 5,
                width: 14,
                height: 14,
                backgroundColor: "#4CAF50",
                borderRadius: "50%",
                border: "2px solid white",
              }}
            />
          </div>
          <span style={{ color: "white", fontSize: "18px", fontWeight: "bold" }}>
            Player 1
          </span>
          <Flex gap="lg" style={{ color: "white", fontSize: "16px", fontWeight: "bold" }}>
            <span>💰 {data.money}</span>
            <span>🛡️ {data.minions}</span>
            <span>⬢ {data.hex}</span>
          </Flex>
        </Flex>
      </Container>

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
          width: "70px", // ปรับขนาดให้เล็กลง
          height: "70px",
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
          left: "14%", // ปรับให้อยู่ด้านซ้าย
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
        onClick={() => router.push("/select_mons")}
      />
      </div>
    </div>
  );
}
