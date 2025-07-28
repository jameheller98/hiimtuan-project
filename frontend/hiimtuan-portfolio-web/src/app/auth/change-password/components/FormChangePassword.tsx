"use client";
import React from "react";
import { useForm } from "react-hook-form";
import Form from "next/form";
import TextInput from "@/ui/TextInput";
import Button from "@/ui/Button";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";

const FormSchema = z.object({
  newPassword: z.string(),
  confirmPassword: z.string(),
});

type FormInput = z.infer<typeof FormSchema>;

export default function FormChangePassword() {
  const { register, handleSubmit } = useForm<FormInput>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      newPassword: "",
      confirmPassword: "",
    },
  });

  return (
    <Form
      action=""
      className="mt-6 text-right"
      onSubmit={handleSubmit((d) => console.log(d))}
    >
      <TextInput
        id="newPassword"
        label="New password"
        type="password"
        autoComplete="off"
        placeholder="At least 8 characters"
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
