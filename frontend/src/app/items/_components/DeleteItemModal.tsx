import { baseUrl } from "@/app/(auth)/_components/SignUp";
import CommonModal from "@/components/CommonModal";
import { API_URL } from "@/constants";
import { useRouter } from "next/navigation";
import React, { SetStateAction } from "react";

type PropsType = {
  isModalOpen: boolean;
  setIsModalOpen: (value: SetStateAction<boolean>) => void;
  itemId: number | undefined;
};

const DeleteItemModalStyles: ReactModal.Styles = {
  overlay: {
    backgroundColor: " rgba(0, 0, 0, 0.4)",
    width: "100%",
    height: "100vh",
    zIndex: "10",
    position: "fixed",
    top: "0",
    left: "0"
  },
  content: {
    width: "36.25rem",
    height: "30%",
    zIndex: "11",
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    borderRadius: "10px",
    backgroundColor: "rgba(255, 255, 255, 100)"
  }
};

const DeleteItemModal = ({ isModalOpen, setIsModalOpen, itemId }: PropsType) => {
  const router = useRouter();
  const deleteItem = async () => {
    try {
      const response = await fetch(`${baseUrl}/api/admin/items/${itemId}`, {
        method: "DELETE",
        credentials: "include",
        headers: {
          Accept: "application/json"
        }
      });

      if (!response.ok) {
        throw new Error("아이템 삭제 실패");
      }

      const data = await response.json();
      setIsModalOpen(false);
      router.push("/");
      return;
    } catch (error) {
      console.error("Error fetching item list:", error);
      return;
    }
  };

  return (
    <CommonModal isOpen={isModalOpen} onRequestClose={() => setIsModalOpen(false)} style={DeleteItemModalStyles}>
      <div className="flex flex-col justify-center items-center p-10 gap-10">
        <p className="text-xl">해당 상품을 삭제하시겠어요?</p>
        <p>상품은 영구적으로 삭제되며, 복구할 수 없습니다.</p>
        <div className="flex gap-20">
          <button onClick={() => setIsModalOpen(false)} className="w-16 h-8 rounded-lg text-lg bg-gray-200">
            취소
          </button>
          <button onClick={deleteItem} className="tw-16 h-8 rounded-lg text-lg bg-gray-300">
            삭제
          </button>
        </div>
      </div>
    </CommonModal>
  );
};

export default DeleteItemModal;
