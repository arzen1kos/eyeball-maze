<div align="center">

  <img width="100" height="100" alt="eyeball-maze-logo" src="https://github.com/user-attachments/assets/671bd7fb-493b-414d-bbfc-fe08e747d965" />

# Eyeball Maze AI

_From game to benchmark: turn a simple maze into an intelligent validation platform for AI agents and operational reliability._

![Java](https://img.shields.io/badge/Java_21%2B-A07F00?style=for-the-badge&logo=openjdk&logoColor=white) 
![Maven](https://img.shields.io/badge/Maven_Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white) 
![K8s Java Client](https://img.shields.io/badge/K8s_Java_Client_API-326CE5?style=for-the-badge&logo=openjdk&logoColor=white) 
![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-FF7800?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)

~⟢~

`EyeballMaze AI` transforms a classic maze challenge into a business-ready platform for evaluating autonomous decision-making, testing AI behavior under pressure, and validating the safety and reliability of intelligent systems before deployment. By combining Java, Kubernetes, and AI-driven gameplay, the project helps teams measure model quality, improve agent performance, and surface edge cases that would otherwise remain hidden in production.

</div>

## **Branch note:** `feature/timer-pause`

**Purpose**: Elapsed-time display and pause/resume that stops the timer and hides the level.

**Notes**

- Depends on `feature/core-game` being merged first.
- Timer must stop on `onPause()` (lifecycle), not just on the in-game pause button.
- Display format: `MM:SS`.

**Status**: Not Started

## Architecture

EyeballMaze AI is designed as a modular system that separates gameplay, evaluation logic, and runtime orchestration.

- Game layer
  - Maze generation and state management
  - Player movement rules and win/lose conditions
  - Scenario definitions (normal, adversarial, benchmark)

- AI layer
  - Strategy agents (random, heuristic, BFS/A*, planned AI)
  - Reasoning and decision loops
  - Reward and policy evaluation
  - Comparison of agent performance across scenarios

- QA and security layer
  - Adversarial environments
  - Prompt/instruction traps
  - Invalid state handling
  - Edge-case validation and regression checks
  - Observability and failure reporting

- Runtime and orchestration
  - Java application services
  - Dockerized deployment
  - Kubernetes orchestration via `kubernetes-client/java`
  - Parallel benchmark execution and environment management

High-level flow:

1. Generate or load a maze scenario.
2. Run an agent against the maze.
3. Capture moves, metrics, failures, and timing.
4. Evaluate correctness, robustness, and safety.
5. Store results and compare performance over time.

## Eyeball Maze model

The Java model is implemented independently of a view, controller, level editor, or
file format. Its public model types are in `src/main/java`, with JUnit 5 tests in
`src/test/java`. The model supports level and square state, goals, eyeball position,
and movement validation. Coordinates use zero-based row and column indexes.

The implemented class diagram is available as
[`docs/model-class-diagram.puml`](./docs/model-class-diagram.puml).

Run the unit tests with `mvn test` using Java 21 or later.

## Local setup instructions

### Prerequisites

- Java 21 or later
- Maven
- Docker
- kubectl
- A local Kubernetes environment such as:
  - Minikube
  - kind
  - Docker Desktop with Kubernetes enabled

### Steps

1. Clone the repo

```bash
git clone https://github.com/arzen1kos/eyeball-maze.git
cd eyeball-maze
git checkout dev
```

2. Build the project

```bash
mvn clean install
```

3. Run the app locally

If you are using a Spring Boot app:

```bash
mvn spring-boot:run
```

If the project includes a Java main entry point:

```bash
mvn exec:java
```

4. Run with Docker

Build the image:

```bash
docker build -t eyeballmaze-ai:local .
```

Run the container:

```bash
docker run -p 8080:8080 eyeballmaze-ai:local
```

5. Validate with Kubernetes locally
Start a local cluster with Minikube or kind:

```bash
minikube start
```

Or:

```bash
kind create cluster
```

Apply a sample deployment manifest:

```bash
kubectl apply -f k8s/deployment.yaml
kubectl get pods
kubectl get svc
```

Check logs:

```bash
kubectl logs -l app=eyeballmaze-ai
```

6. Run tests

```bash
mvn test
```

7. Useful development workflow

- Make changes on `dev`
- Run tests locally
- Validate Docker build
- Smoke-test Kubernetes deployment
- Commit and push
- Merge to `main` only after stable validation

## Project goals

- Create a practical AI navigation benchmark
- Measure reasoning quality under uncertainty
- Expose adversarial conditions for QA and security testing
- Show how Java-based services can integrate with Kubernetes
- Build an extensible platform for future AI and automation experiments

## Next steps

- Add a `CountDownTimer` or `Handler`-based elapsed-time tracker
- Implement pause overlay that hides the maze and stops the clock
- Hook into `onPause()`/`onResume()` so the timer survives config changes

---

Created by Arsenie — 2025
