"use client";

import { baseUrl } from "@/app/(auth)/_components/SignUp";
import { useItemStore } from "@/store/itemStore";
import React from "react";

type ItemEditPropsType = {
  itemId: number;
};

const EditItem = ({ itemId }: ItemEditPropsType) => {
  const {
    name,
    price,
    stockQuantity,
    itemImage,
    itemInfoImage,
    setName,
    setPrice,
    setStockQuantity,
    setItemImage,
    setItemInfoImage
  } = useItemStore();

  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };
  const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPrice(+e.target.value);
  };
  const handleStockQuantityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setStockQuantity(+e.target.value);
  };
  const handleItemImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setItemImage(file);
    }
  };

  const handleItemInfoImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setItemInfoImage(file);
    }
  };

  const handleEditItemInfo = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = new FormData();
    if (itemImage) formData.append("itemImage", itemImage);
    if (itemInfoImage) formData.append("itemInfoImage", itemInfoImage);

    try {
      const response = await fetch(`${baseUrl}/api/admin/items/${itemId}`, {
        method: "POST",
        credentials: "include",
        body: formData
      });

      const result = await response.json();
      console.log(result);

      if (response.ok) {
        alert("상품 생성 성공");
      } else {
        alert(`상품 생성 실패: ${result.message}`);
      }
    } catch (error) {
      console.error("요청 중 오류 발생:", error);
      alert("상품 생성 중 오류가 발생했습니다.");
    }
  };

  const handleEditItemImages = async () => {
    console.log("ㅠㅠ");
  };

  return (
    <>
      <form className="flex flex-col text-xl" onSubmit={handleEditItemInfo}>
        <label htmlFor="name" className="indent-3 mb-2">
          상품 이름
        </label>
        <input
          id="name"
          type="text"
          defaultValue={name}
          value={name}
          onChange={handleNameChange}
          placeholder="상품 이름"
          className="w-[36.25rem] h-[3.75rem] indent-5 rounded-[35px] bg-gray-200"
        />
        <label htmlFor="price" className="indent-3 mb-2">
          상품 가격
        </label>
        <input
          id="price"
          type="number"
          defaultValue={price}
          value={price}
          onChange={handlePriceChange}
          placeholder="상품 가격"
          className="w-[36.25rem] h-[3.75rem] indent-5 rounded-[35px] bg-gray-200"
        />
        <label htmlFor="stockQuantity" className="indent-3 mb-2">
          상품 수량
        </label>
        <input
          id="stockQuantity"
          type="number"
          defaultValue={stockQuantity}
          value={stockQuantity}
          onChange={handleStockQuantityChange}
          placeholder="상품 수량"
          className="w-[36.25rem] h-[3.75rem] indent-5 rounded-[35px] bg-gray-200"
        />

        <button>상품 정보 수정</button>
      </form>
      <h2>상품 이미지 변경</h2>
      <form className="flex flex-col text-xl" onSubmit={handleEditItemImages}>
        <label htmlFor="content" className="indent-3 mb-2">
          상품 대표 이미지
        </label>
        <input
          id="content"
          type="file"
          onChange={handleItemImageChange}
          className="w-[36.25rem] h-[3.75rem] indent-5 rounded-[35px] bg-gray-200"
        />
        <label htmlFor="content" className="indent-3 mb-2">
          상품 설명 이미지
        </label>
        <input
          id="content"
          type="file"
          onChange={handleItemInfoImageChange}
          className="w-[36.25rem] h-[3.75rem] indent-5 rounded-[35px] bg-gray-200"
        />
      </form>
    </>
  );
};

export default EditItem;
