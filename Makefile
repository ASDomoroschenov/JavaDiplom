.PHONY: check-deps

check-deps:
	@echo "Проверка установленных зависимостей..."
	@command -v fplll >/dev/null 2>&1 && echo "+ fplll установлен" || echo "- fplll не найден"
	@command -v sage >/dev/null 2>&1 && echo "+ SageMath установлен" || echo "- SageMath не найден"