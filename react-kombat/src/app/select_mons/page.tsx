"use client";
import React from 'react'
import { useRouter } from "next/navigation";
import { Flex, Radio, RadioGroup ,Button} from '@mantine/core';
export default function selectmons() {
   const router = useRouter();
  return (
    <div>
     {/* พื้นหลัง */}
     <div
        style={{
          width: "100%",
          height: "100vh",
          position: "fixed",
          top: 0,
          left: 0,
          backgroundImage: "url(/image/Desktop_select_mons.jpg)",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          zIndex: -1,
        }}
      />
      <div
  className="d-flex justify-content-center align-items-center gap-5"
  style={{
    position: "fixed",
    top: "50%", // ตั้งให้อยู่ตรงกลางแนวตั้ง
    left: "50%", // ตั้งให้อยู่ตรงกลางแนวนอน
    transform: "translate(-50%, -50%)", // เลื่อนกลับครึ่งหนึ่งของขนาดตัวเองเพื่อให้กลางจุด
    zIndex: 10, // ให้มันอยู่เหนือทุกอย่าง
  }}
>
  <img
    src="/image/pandy.png"
    className="rounded img-fluid"
    alt="mode1"
    style={{
      maxWidth: "200px",
      marginBottom: "3vh",
    }}
    onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.9")}
    onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
  />
  <img
    src="/image/cheese_bear.png"
    className="rounded img-fluid"
    alt="mode2"
    style={{
      maxWidth: "200px",
      marginBottom: "3vh",
    }}
    onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.9")}
    onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
  />
  <img
    src="/image/moonum.png"
    className="rounded img-fluid"
    alt="mode3"
    style={{
      maxWidth: "200px",
      marginBottom: "3vh",
    }}
    onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.9")}
    onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
  />
</div>
<Flex
      direction={{ base: 'column', sm: 'row' }}
  
      gap= "10%"
     
      justify="center"
      style={{
        position: "fixed",
        bottom: "23%", // ปรับให้ลงมาต่ำกว่าขอบจอเล็กน้อย
        left: "50%",
        transform: "translateX(-50%)", // ทำให้ปุ่มอยู่กึ่งกลางแนวนอน
        width: "100%", // ทำให้ปุ่มกระจายเท่ากัน
        
        
      }}
    >
      <img
          src="/image/selectmons.png"
          alt="Play Button"
          style={{
            width: "20vw",
            maxWidth: "150px",
            
            height: "auto",
            cursor: "pointer",
            transition: "0.3s",
          }}
          onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
          onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
          onClick={() => router.push("/playstate1")}
        />
       <img
          src="/image/selectmons.png"
          alt="Play Button"
          style={{
            width: "20vw",
            maxWidth: "150px",
            
            height: "auto",
            cursor: "pointer",
            transition: "0.3s",
          }}
          onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
          onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
          onClick={() => router.push("/playstate1")}
        />
       <img
          src="/image/selectmons.png"
          alt="Play Button"
          style={{
            width: "20vw",
            maxWidth: "150px",
            
            height: "auto",
            cursor: "pointer",
            transition: "0.3s",
          }}
          onMouseEnter={(e) => (e.currentTarget.style.opacity = "0.8")}
          onMouseLeave={(e) => (e.currentTarget.style.opacity = "1")}
          onClick={() => router.push("/playstate1")}
        />
    </Flex>
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
        onClick={() => router.push("/playgame_p1")}
      />
    </div>
  )
}
