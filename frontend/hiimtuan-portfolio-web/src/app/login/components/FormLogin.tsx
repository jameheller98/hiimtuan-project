"use client";
import React from "react";
import { useForm } from "react-hook-form";
import Form from "next/form";
import TextInput from "@/ui/TextInput";
import Button from "@/ui/Button";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";

const FormSchema = z.object({
  email: z.string(),
  password: z.string(),
});

type FormInput = z.infer<typeof FormSchema>;

export default function FormLogin() {
  const { register, handleSubmit } = useForm<FormInput>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  return (
    <Form
      action=""
      className="mt-6 text-right"
      onSubmit={handleSubmit((d) => console.log(d))}
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
      <Button className="text-(--color-blue) text-sm mt-4" variant="text">
        Forgot Password?
      </Button>
      <Button className="mt-4">Sign in</Button>
    </Form>
  );
}
