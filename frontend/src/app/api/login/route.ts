import { API_URL } from "@/constants";
import { NextRequest, NextResponse } from "next/server";

export async function POST(request: NextRequest) {
  try {
    const requestData = await request.json();
    const response = await fetch(`${API_URL}/api/login`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(requestData)
    });

    const data = await response.json();

    return new NextResponse(JSON.stringify(data), {
      status: response.status,
      headers: { "Content-Type": "application/json" }
    });
  } catch (error) {
    console.error("error", error);
    return new NextResponse(JSON.stringify({ message: "Something went wrong!" }), {
      status: 500,
      headers: { "Content-Type": "application/json" }
    });
  }
}
