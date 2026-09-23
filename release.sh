#!/usr/bin/env bash
set -euo pipefail

# Read version from build.gradle
VERSION=$(grep "^version" build.gradle | sed "s/version = '\(.*\)'/\1/")
if [[ -z "$VERSION" ]]; then
    echo "Error: could not read version from build.gradle"
    exit 1
fi
TAG="v${VERSION}"

echo "Version from build.gradle: ${VERSION}"
echo "Git tag: ${TAG}"

# Ensure we're on master and up to date
BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [[ "$BRANCH" != "master" ]]; then
    echo "Error: not on master branch (currently on '${BRANCH}')"
    exit 1
fi

git pull --ff-only origin master

# Check if tag already exists
if git rev-parse "$TAG" >/dev/null 2>&1; then
    echo "Tag ${TAG} already exists. Bump the version in build.gradle first."
    exit 1
fi

# Confirm before tagging
read -rp "Create and push tag ${TAG}? [y/N] " confirm
if [[ "$confirm" != [yY] ]]; then
    echo "Aborted."
    exit 0
fi

git tag "$TAG"
git push origin "$TAG"

echo "Tag ${TAG} pushed. CI will build the release."
