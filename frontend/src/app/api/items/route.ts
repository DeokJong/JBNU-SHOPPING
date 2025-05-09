// import { NextRequest, NextResponse } from "next/server";

// export async function POST(request: Request) {
//   try {
//     const body = await request.json(); // 요청 데이터 파싱
//     console.log("Request Body:", body); // 요청 데이터 확인

//     const backendResponse = await fetch(`${baseUrl}/api/items", {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/json"
//       },
//       body: JSON.stringify(body) // 백엔드로 전송
//     });

//     const backendData = await backendResponse.json();
//     console.log("Backend Response:", backendData); // 백엔드 응답 확인

//     if (backendResponse.ok) {
//       return NextResponse.json(backendData, { status: 201 });
//     } else {
//       return NextResponse.json(
//         { message: "Failed to create item", error: backendData },
//         { status: backendResponse.status }
//       );
//     }
//   } catch (error) {
//     console.error("Error"); // 에러 로그
//     return NextResponse.json({ message: "Server error" }, { status: 500 });
//   }
// }
export {}