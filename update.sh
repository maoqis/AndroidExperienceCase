#!/bin/bash
git submodule foreach git branch checkout master
git submodule foreach git pull --rebase
git pull --rebase
