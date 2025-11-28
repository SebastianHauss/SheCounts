
# Git Workflow

This project uses a simple and beginner-friendly Git workflow.

---

## 🌳 Branch Structure

We use these branches:

- `main` → Stable, production-ready code
- `develop` → Active development branch
- `feature/*` → New features

Example:

```

main
develop
feature/login-page
feature/user-comments

````

---

## 🚀 How to Work on a Feature

### 1. Switch to `develop` and update it

```bash
git checkout develop
git pull origin develop
````

---

### 2. Create a new feature branch

```bash
git checkout -b feature/your-feature-name
```

Examples:

```bash
git checkout -b feature/navbar
git checkout -b feature/login-form
```

---

### 3. Make changes and commit

```bash
git add .
git commit -m "Add short description of change"
```

---

### 4. Push your branch to GitHub

```bash
git push -u origin feature/your-feature-name
```

---

### 5. Create a Pull Request (PR)

On GitHub:

* Base branch: `develop`
* Compare branch: your `feature/*` branch

Wait for review, then merge.

---

## ✅ Merging Rules

✅ Always merge into `develop` via Pull Request ❌ Never push directly to `main`

---

## 🚢 Release Process (develop → main)

Only when the team is ready to release:

```bash
git checkout main
git pull origin main
git merge develop
git push origin main
```

---

## 🐛 Bug Fixes

For quick fixes:

```bash
git checkout develop
git checkout -b fix/short-description
```

Follow the same PR process.

---

## 🧠 Team Rules

✅ One feature per branch
✅ Clear commit messages
✅ Keep branches small and focused

---

Happy coding 🚀