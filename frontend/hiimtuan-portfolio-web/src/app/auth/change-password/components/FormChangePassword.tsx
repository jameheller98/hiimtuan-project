"use client";
import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import Form from "next/form";
import TextInput from "@/ui/TextInput";
import Button from "@/ui/Button";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { useChangePasswordMutation } from "@/api/mutations/auth/useChangePasswordMutation";
import { useSearchParams, useRouter } from "next/navigation";
import { toast } from "react-toastify";
import useCheckIsLogin from "@/hooks/useCheckIsLogin";

const FormSchema = z
  .object({
    newPassword: z
      .string()
      .nonempty("Password is required")
      .min(6, "At least 6 characters"),
    confirmPassword: z.string(),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: "Password do not match.",
    path: ["confirmPassword"],
  });

type FormInput = z.infer<typeof FormSchema>;

export default function FormChangePassword() {
  const searchParams = useSearchParams();
  const token = searchParams.get("token");
  const router = useRouter();
  const { trigger } = useChangePasswordMutation();
  const { register, handleSubmit } = useForm<FormInput>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      newPassword: "",
      confirmPassword: "",
    },
  });

  useCheckIsLogin();

  useEffect(() => {
    if (!token) {
      router.replace("/auth/login");
    }
  }, [token, router]);

  const handleChangePassword = async (data: FormInput) => {
    if (!token) return;

    try {
      const result = await trigger({
        password: data.newPassword,
        passwordResetToken: token,
      });

      toast(result.message, { type: "success", position: "top-left" });
      router.replace("/auth/login");
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <Form
      action=""
      className="mt-6 text-right"
      onSubmit={handleSubmit(handleChangePassword)}
    >
      <TextInput
        id="newPassword"
        label="New password"
        type="password"
        autoComplete="off"
        placeholder="At least 6 characters"
        {...register("newPassword")}
      />
      <TextInput
        id="confirmPassword"
        label="Confirm password"
        type="password"
        autoComplete="off"
        classNameContainer="mt-4"
        placeholder="Verify password"
        {...register("confirmPassword")}
      />
      <Button className="mt-6">Change password</Button>
    </Form>
  );
}
