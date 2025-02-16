"use client";
import React, { useState, useEffect } from "react";
import { Container, Flex } from "@mantine/core";
import { useRouter } from "next/navigation";

const Navbar2 = () => {
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
                src="/image/moodend9.jpg"
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
    </div>
  );
};

export default Navbar2;
