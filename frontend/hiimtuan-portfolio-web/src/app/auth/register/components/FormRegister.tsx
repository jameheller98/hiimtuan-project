"use client";
import React from "react";
import { useForm } from "react-hook-form";
import Form from "next/form";
import TextInput from "@/components/ui/TextInput";
import Button from "@/components/ui/Button";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import useCheckIsLogin from "@/hooks/useCheckIsLogin";
import { useRegisterMutation } from "@/api/mutations/auth/useRegisterMutation";
import { toast } from "react-toastify";
import { useRouter } from "next/navigation";
import { AxiosError } from "axios";

const FormSchema = z
  .object({
    fullName: z.string().nonempty("Fullname is required"),
    email: z.email("Email invalid"),
    password: z
      .string()
      .nonempty("Password is required")
      .min(6, "At least 6 characters"),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Password do not match.",
    path: ["confirmPassword"],
  });

type FormInput = z.infer<typeof FormSchema>;

export default function FormRegister() {
  const router = useRouter();
  const { trigger } = useRegisterMutation();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormInput>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      fullName: "",
      email: "",
      password: "",
      confirmPassword: "",
    },
  });

  useCheckIsLogin();

  const handleRegister = async (data: FormInput) => {
    try {
      const result = await trigger({
        fullName: data.fullName,
        email: data.email,
        password: data.password,
      });

      toast(result.message, { type: "success", position: "top-left" });
      router.replace("/auth/login");
    } catch (e) {
      console.log(e);
      const error = e as AxiosError<{ message: string; detail: string }>;
      if (error?.request?.status === 0) {
        toast.error("Server error! Please comeback later.");
      }

      if (error?.response?.status === 400) {
        toast.error(
          error?.response?.data?.detail ||
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
      onSubmit={handleSubmit(handleRegister)}
    >
      <TextInput
        id="fullName"
        label="Fullname"
        type="string"
        autoComplete="off"
        placeholder="Nguyen Van A"
        {...register("fullName")}
        error={errors.fullName?.message}
      />
      <TextInput
        id="email"
        label="Email"
        type="email"
        autoComplete="off"
        classNameContainer="mt-4"
        placeholder="Example@email.com"
        {...register("email")}
        error={errors.email?.message}
      />
      <TextInput
        id="password"
        label="Password"
        type="password"
        autoComplete="off"
        classNameContainer="mt-4"
        placeholder="At least 6 characters"
        {...register("password")}
        error={errors.password?.message}
      />
      <TextInput
        id="confirmPassword"
        label="Confirm password"
        type="password"
        autoComplete="off"
        classNameContainer="mt-4"
        placeholder="Verify password"
        {...register("confirmPassword")}
        error={errors.confirmPassword?.message}
      />
      <Button className="mt-6">Sign up</Button>
    </Form>
  );
}
