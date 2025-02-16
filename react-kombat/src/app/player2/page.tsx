"use client";
import React, { useState, useEffect } from "react";
import { Container, Flex } from "@mantine/core";

export default function player2() {
  const [stats, setStats] = useState({ money: 0, minions: 0, hex: 0 });

  useEffect(() => {
    // เรียก API ดึงข้อมูลจาก backend
    fetch("/api/game-stats")
      .then((res) => res.json())
      .then((data) => setStats(data))
      .catch((err) => console.error("Error fetching stats:", err));
  }, []);

  return (
    <div>
      {/* Navbar */}
      <Container
        size={780}
        style={{ background: "#6D2323", borderRadius: "10px", padding: "10px" }}
      >
        <Flex align="center" justify="space-between" gap="md">
          {/* รูปโปรไฟล์ */}
          <div style={{ position: "relative" }}>
            <div
              style={{
                width: 75,
                height: 75,
                borderRadius: "50%",
                border: "3px solid #6D2323",
                backgroundColor: "white",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <img
                src="/image/moodeng9.jpg"
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

          {/* Username */}
          <span style={{ color: "white", fontSize: "18px", fontWeight: "bold" }}>Username</span>

          {/* แสดงค่าเงิน, มินเนี่ยน และ Hex จาก backend */}
          <Flex gap="lg" style={{ color: "white", fontSize: "16px", fontWeight: "bold" }}>
            <span>💰 {stats.money}</span> {/* จำนวนเงิน */}
            <span>🛡️ {stats.minions}</span> {/* จำนวนมินเนี่ยน */}
            <span>⬢ {stats.hex}</span> {/* จำนวน Hex */}
          </Flex>
        </Flex>
      </Container>
    </div>
  );
}
