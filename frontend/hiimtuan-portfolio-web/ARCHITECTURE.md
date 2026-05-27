# hiimtuan-portfolio-web — Next.js Architecture

Next.js 14+ with App Router, TypeScript, TailwindCSS.

## Structure

```
src/
├── api/
│   ├── api.interface.ts             # Shared API types / response shapes
│   ├── index.ts                     # Axios/fetch instance, base config
│   ├── mutations/
│   │   └── auth/                    # useLoginMutation, useRegisterMutation,
│   │                                #   useLogoutMutation, useResetPasswordMutation,
│   │                                #   useChangePasswordMutation
│   └── queries/
│       └── auth/                    # useProfileMutation (profile fetch)
│
├── app/                             # Next.js App Router
│   ├── layout.tsx                   # Root layout (providers, Header, Footer)
│   ├── page.tsx                     # Home page
│   ├── globals.css
│   ├── components/                  # Layout-level components (Header, Footer, BackIcon, SlideTransition)
│   └── auth/
│       ├── login/
│       │   ├── page.tsx
│       │   └── components/FormLogin.tsx
│       ├── register/
│       │   ├── page.tsx
│       │   └── components/FormRegister.tsx
│       ├── forgot-password/
│       │   ├── page.tsx
│       │   └── components/FormForgotPassword.tsx
│       └── change-password/
│           ├── page.tsx
│           └── components/FormChangePassword.tsx
│
├── hooks/
│   ├── useCheckIsLogin.ts           # Redirects unauthenticated users
│   ├── useClickOutside.ts           # Detects clicks outside a ref element
│   └── useIsMobile.ts               # Responsive breakpoint detection
│
├── stores/
│   ├── index.ts                     # Combined Zustand store
│   └── authStore/
│       ├── useAuthStore.ts          # Auth store hook
│       └── createTokenSlice.ts      # Token slice (access token state)
│
└── ui/                              # Reusable primitive components
    ├── Button.tsx
    └── TextInput.tsx
```

## State Management — Zustand

Auth token is held in a Zustand slice (`createTokenSlice`). The store is composed in `stores/index.ts` and accessed via `useAuthStore()`.

Token is stored in Zustand (in-memory) — not in localStorage. On page refresh the user is re-authenticated via the `useCheckIsLogin` hook which calls the profile query.

## Data Fetching — TanStack Query

- **Mutations** (`src/api/mutations/`) — all write operations: login, register, logout, reset/change password
- **Queries** (`src/api/queries/`) — read operations: profile fetch

Convention: one file per operation, named `use<Action><Resource>Mutation` or `use<Resource>Query`.

## Routing

App Router with file-based routing. Auth pages are grouped under `app/auth/`.

Each page keeps only routing/layout concerns; form logic lives in a co-located `components/Form<Page>.tsx` component.

## Key Dependencies

| Package | Purpose |
|---|---|
| Next.js (App Router) | Framework + SSR |
| TypeScript | Type safety |
| TailwindCSS | Styling |
| Zustand | Client auth state |
| TanStack Query | Server state, mutations |
