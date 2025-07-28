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
});

type FormInput = z.infer<typeof FormSchema>;

export default function FormForgotPassword() {
  const { register, handleSubmit } = useForm<FormInput>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      email: "",
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
      <Button className="mt-6">Send</Button>
    </Form>
  );
}
