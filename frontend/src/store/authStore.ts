import { AuthStoreType } from "@/types/auth.type";
import { create } from "zustand";

export const useAuthStore = create<AuthStoreType>((set) => ({
  username: "",
  password: "",
  passwordConfirm: "",
  name: "",
  phoneNumber: "",
  email: "",
  zipcode: 0,
  street: "",
  detail: "",
  error: {
    username: "",
    password: "",
    passwordConfirm: "",
    name: "",
    phoneNumber: "",
    email: "",
    zipcode: "",
    street: "",
    detail: ""
  },

  setUsername: (username: string) => set({ username }),
  setPassword: (password: string) => set({ password }),
  setPasswordConfirm: (passwordConfirm: string) => set({ passwordConfirm }),
  setName: (name: string) => set({ name }),
  setPhoneNumber: (phoneNumber: string) => set({ phoneNumber }),
  setEmail: (email: string) => set({ email }),
  setZipcode: (zipcode: number) => set({ zipcode }),
  setStreet: (street: string) => set({ street }),
  setDetail: (detail: string) => set({ detail }),
  setError: (error) => set((state) => ({ error: { ...state.error, error } }))
}));
