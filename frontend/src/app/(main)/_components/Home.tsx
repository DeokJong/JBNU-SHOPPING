"use client";

import React, { useEffect, useState } from "react";
import GoodsCard, { Item } from "./GoodsCard";
import usePageStore from "@/store/pageStore";
import Link from "next/link";
import { baseUrl } from "@/app/(auth)/_components/SignUp";
import useCategoryStore from "@/store/categoryStore";

const getItemList = async (pageNumber: number, category: string) => {
  if (category === "LIKED_BY_ME") {
    try {
      const response = await fetch(`${baseUrl}/api/public/items?page=${pageNumber}&itemSort=CREATED_DATE`, {
        method: "GET",
        credentials: "include",
        headers: {
          Accept: "application/json"
        }
      });

      if (!response.ok) {
        throw new Error("아이템 목록을 가져오는 데 실패했습니다.");
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error("Error fetching item list:", error);
      return null;
    }
  }
  try {
    const response = await fetch(`${baseUrl}/api/public/items?page=${pageNumber}&itemSort=${category}`, {
      method: "GET",
      credentials: "include",
      headers: {
        Accept: "application/json"
      }
    });

    if (!response.ok) {
      throw new Error("아이템 목록을 가져오는 데 실패했습니다.");
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching item list:", error);
    return null;
  }
};

export const refreshToken = async () => {
  try {
    const response = await fetch(`${baseUrl}/api/auth/refresh`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json"
      }
    });

    if (!response.ok) {
      throw new Error(`토큰 갱신 실패: ${response.statusText}`);
    }

    const data = await response.json();
    return true;
  } catch (error) {
    return false;
  }
};

const Home = () => {
  const numList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  const { category } = useCategoryStore();
  const { pageNumber, setPageNumber } = usePageStore();
  const [items, setItems] = useState<Item[]>([]);

  const fetchItems = async (pageNumber: number, category: string) => {
    // TODO: 페이지네이션
    const result = await getItemList(pageNumber, category);
    if (result && result.data.items) {
      const filteredItems =
        category === "LIKED_BY_ME" ? result.data.items.filter((item: Item) => item.likedByMe) : result.data.items;
      setItems(filteredItems);
    }
  };

  useEffect(() => {
    refreshToken();
    fetchItems(pageNumber, category);
    //eslint-disable-next-line
  }, [pageNumber, category]);

  return (
    <div className="flex flex-col items-center">
      <ul className="w-[73.75rem] flex flex-wrap gap-5  items-center">
        {items.length === 0 ? (
          <p className="w-full text-center text-lg font-semibold mt-5">상품이 없습니다</p>
        ) : (
          items.map((item, index) => (
            <li key={index} className="mb-[1.25rem]">
              <Link href={`/items/detail/${item.itemId}`}>
                <GoodsCard item={item} />
              </Link>
            </li>
          ))
        )}
      </ul>
      <div className="flex gap-10 h-40">
        {numList.map((num) => {
          return (
            <button key={num} onClick={() => setPageNumber(num)} className="text-lg mb-10 mt-5">
              {num}
            </button>
          );
        })}
      </div>
    </div>
  );
};

export default Home;
