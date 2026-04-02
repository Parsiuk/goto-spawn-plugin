#!/usr/bin/env bash
set -euo pipefail

# Read version from pom.xml
VERSION=$(grep -m1 '<version>' pom.xml | sed 's/.*<version>\(.*\)<\/version>.*/\1/')
TAG="v${VERSION}"

echo "Version from pom.xml: ${VERSION}"
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
    echo "Tag ${TAG} already exists. Bump the version in pom.xml first."
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
