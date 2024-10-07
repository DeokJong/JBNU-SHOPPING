import { AuthStoreType } from "@/types/auth.type";
import { create } from "zustand";

export const useAuthStore = create<AuthStoreType>((set) => ({
  username: "",
  password: "",
  passwordConfirm: "",
  name: "",
  phoneNumber: "",
  email: "",
  error: {
    username: "",
    password: "",
    passwordConfirm: "",
    name: "",
    phoneNumber: "",
    email: ""
  },

  setUsername: (username: string) => set({ username }),
  setPassword: (password: string) => set({ password }),
  setPasswordConfirm: (passwordConfirm: string) => set({ passwordConfirm }),
  setName: (name: string) => set({ name }),
  setPhoneNumber: (phoneNumber: string) => set({ phoneNumber }),
  setEmail: (email: string) => set({ email }),
  setError: (error) => set((state) => ({ error: { ...state.error, error } }))
}));
