"use client";
import React from "react";
import { useForm } from "react-hook-form";
import Form from "next/form";
import TextInput from "@/ui/TextInput";
import Button from "@/ui/Button";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { useResetPasswordMutation } from "@/api/mutations/auth/useResetPasswordMutation";
import { toast } from "react-toastify";
import { useRouter } from "next/navigation";
import useCheckIsLogin from "@/hooks/useCheckIsLogin";

const FormSchema = z.object({
  email: z.string(),
});

type FormInput = z.infer<typeof FormSchema>;

export default function FormForgotPassword() {
  const router = useRouter();
  const { trigger } = useResetPasswordMutation();
  const { register, handleSubmit } = useForm<FormInput>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      email: "",
    },
  });

  useCheckIsLogin();

  const handleResetPassword = async (data: FormInput) => {
    try {
      const result = await trigger(data);

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
      onSubmit={handleSubmit(handleResetPassword)}
    >
      <TextInput
        id="email"
        label="Email"
        type="email"
        autoComplete="off"
        placeholder="Example@email.com"
        {...register("email")}
      />
      <Button className="mt-6">Send</Button>
    </Form>
  );
}
