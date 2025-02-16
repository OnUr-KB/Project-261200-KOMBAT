// pages/yourPage.tsx
"use client"
import React from "react";
import { useRouter } from "next/navigation";
import { Container, TextInput,Input,Textarea} from "@mantine/core";
import Navbar from "../components/navbar1"; // สำหรับไฟล์ที่อยู่ในโฟลเดอร์เดียวกันกับหน้า
 

const strategypage = () => {
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
          backgroundImage: "url('/image/Desktop_strategy01.jpg')",
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
          left: "4%",
          margin: 0,
          position :"fixed"
        }}
      >
        

        {/* ปุ่ม */}
        <img
          src="/image/return_button.png"
          alt="Return Button"
          style={{
            position: "absolute",
            top: "87%",
            
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

      </div>
      <div
  style={{
    display: "flex",
    flexDirection: "column", // จัดเรียงทุกอย่างในแนวตั้ง
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
    paddingTop:"10%", // ปรับระยะห่างใต้ Navbar
   
  }}
>
  {/* กล่อง Input 1 */}
  <Container>
    <TextInput mt="md" radius="xl" placeholder="name:" />
  </Container>

  {/* กล่อง Input 2 */}
  <Container style={{ marginTop: "20px" }}> 
    <Input size="sm" radius="xl" placeholder="defense:" />
  </Container>
 {/* กล่อง strategy */}
<Container
  style={{
    marginTop: "30px",
    width: "60vw", // ปรับให้กว้างขึ้น
    height: "80vh", // ปรับให้สูงขึ้น
  }}
>
  <Textarea
    resize="vertical"
    label="Strategy"
    placeholder="Describe your strategy..."
    style={{
      width: "100%", // ให้เต็ม Container
      minHeight: "55vh", // ปรับให้สูงขึ้น
      fontSize: "18px", // ปรับขนาดตัวอักษรให้ใหญ่ขึ้น
      padding: "10px", // เพิ่มระยะห่างข้างใน
    }}
  />
</Container>

  
</div>
{/* ปุ่ม next */}
<img
        src="/image/button_Finish_.png"
        alt="Finish Button"
        style={{
          width: "20vw", // ใช้เป็น % ของ viewport width
          maxWidth: "300px", // จำกัดขนาดสูงสุด
          position: "absolute",
          top: "87%",
          left: "42%",
          height: "auto",
          cursor: "pointer",
          transition: "0.3s",

         // marginTop: "200px",
          //marginBottom: "2px", // ระยะห่างระหว่างปุ่ม
        }}
        onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
        onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
        onClick={() => router.push("/create_strategy2")}
      />

      </div>
   
  );
};

export default strategypage;
