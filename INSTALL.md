# Установка зависимостей

Для работы проекта необходимо установить следующие утилиты:

- **fplll** — библиотека для работы с решётками (алгоритм LLL)
- **SageMath** — система компьютерной алгебры, используется для вычисления Gröbner-баз

## Установка

### fplll (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install fplll
```

Если требуется более свежая версия, можно собрать из исходников:

```bash
sudo apt install build-essential autoconf libtool libgmp-dev libmpfr-dev libmpfi-dev

git clone https://github.com/fplll/fplll.git
cd fplll
./autogen.sh
./configure
make
sudo make install
```

### SageMath (Linux)

```bash
wget https://mirrors.mit.edu/sage/linux/64bit/sage-*.tar.bz2
# замените * на актуальную версию

tar -xvjf sage-*.tar.bz2
cd sage-*
./sage
```

Чтобы добавить sage в PATH:

```bash
sudo ln -s $(pwd)/sage /usr/local/bin/sage
```

### Проверка установки

Вы можете использовать Makefile или bash-скрипт для проверки:

#### Makefile

```bash
make check-deps
```

Выполнение этого файла выведет статус по установленным компонентам.