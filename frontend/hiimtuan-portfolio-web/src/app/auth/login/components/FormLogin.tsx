"use client";
import React from "react";
import { useForm } from "react-hook-form";
import Form from "next/form";
import TextInput from "@/ui/TextInput";
import Button from "@/ui/Button";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import Link from "next/link";
import { useLoginMutation } from "@/api/mutations/auth/useLoginMutation";
import { useAuthStore } from "@/stores";
import { useRouter } from "next/navigation";
import { AxiosError } from "axios";
import { toast } from "react-toastify";
import useCheckIsLogin from "@/hooks/useCheckIsLogin";

const FormSchema = z.object({
  email: z.string(),
  password: z.string(),
});

type FormInput = z.infer<typeof FormSchema>;

export default function FormLogin() {
  const router = useRouter();
  const { setAllToken } = useAuthStore();
  const { trigger } = useLoginMutation();
  const { register, handleSubmit } = useForm<FormInput>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  useCheckIsLogin();

  const handleLogin = async (data: FormInput) => {
    try {
      const result = await trigger(data);

      if (result.status === 200 && result.data) {
        setAllToken(result.data.token, result.data.refreshToken);
        router.replace("/");
      }
    } catch (e) {
      console.log(e);
      const error = e as AxiosError<{ message: string }>;
      if (error?.request?.status === 0) {
        toast.error("Server error! Please comeback later.");
      }

      if (error?.response?.status === 401) {
        toast.error(
          error?.response?.data?.message ||
            "Server error! Please comeback later."
        );
      }
    }
  };

  return (
    <Form
      action=""
      className="mt-6 text-right"
      onSubmit={handleSubmit(handleLogin)}
    >
      <TextInput
        id="email"
        label="Email"
        type="email"
        autoComplete="off"
        placeholder="Example@email.com"
        {...register("email")}
      />
      <TextInput
        id="password"
        label="Password"
        type="password"
        autoComplete="off"
        classNameContainer="mt-4"
        placeholder="At least 8 characters"
        {...register("password")}
      />
      <Link
        href="/auth/forgot-password"
        className="text-(--color-blue) inline-block text-sm mt-4"
      >
        Forgot Password?
      </Link>
      <Button className="mt-4">Sign in</Button>
    </Form>
  );
}
