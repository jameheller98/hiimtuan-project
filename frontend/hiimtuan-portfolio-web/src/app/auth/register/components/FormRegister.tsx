"use client";
import React from "react";
import { useForm } from "react-hook-form";
import Form from "next/form";
import TextInput from "@/ui/TextInput";
import Button from "@/ui/Button";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";

const FormSchema = z.object({
  fullName: z.string(),
  email: z.string(),
  password: z.string(),
  confirmPassword: z.string(),
});

type FormInput = z.infer<typeof FormSchema>;

export default function FormRegister() {
  const { register, handleSubmit } = useForm<FormInput>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      fullName: "",
      email: "",
      password: "",
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
        id="fullName"
        label="Fullname"
        type="string"
        autoComplete="off"
        placeholder="Nguyen Van A"
        {...register("fullName")}
      />
      <TextInput
        id="email"
        label="Email"
        type="email"
        autoComplete="off"
        classNameContainer="mt-4"
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
      <TextInput
        id="confirmPassword"
        label="Confirm password"
        type="password"
        autoComplete="off"
        classNameContainer="mt-4"
        placeholder="Verify password"
        {...register("confirmPassword")}
      />
      <Button className="mt-6">Sign up</Button>
    </Form>
  );
}
