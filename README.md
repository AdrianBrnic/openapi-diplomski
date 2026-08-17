# OpenAPI Diplomski Rad — API Design Approaches Demo

Demo system for exploring the OpenAPI Specification and comparing
**code-first** and **specification-first** approaches to HTTP API development.
Built as part of a Master's thesis.

## Overview

This project implements the same backend functionality using two different
API development approaches, in order to compare their trade-offs in practice:

- **`backend-codefirst`** — Code-first approach: the API was implemented
  directly in code (Spring Boot controllers/annotations), with the OpenAPI
  specification generated afterward from the implementation.
- **`backend`** / **`backend-v2`** — Specification-first approach: the
  OpenAPI specification was designed first, then used to guide the
  implementation of the API.

## Tech Stack

- **Backend:** Java, Spring Boot, PostgreSQL
- **Frontend:** React
- **API Spec:** OpenAPI 3.1

## Thesis

The full thesis document (in Croatian) is available in [`docs/`](./docs).