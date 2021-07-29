# algonated-code-runner

![Logo](./img/logo.svg)

Algonated is short for Algorithms Animated. This was my final year project in the Brunel University Computer Science
Degree. Algonated is meant to be a platform that can be used by anyone to write algorithms in Java to solve well-known
heuristic search problems like
[The Scales Problem](https://www.mathmammoth.com/preview/balance_problems.pdf)
and [The Travelling Salesman Problem](https://en.wikipedia.org/wiki/Travelling_salesman_problem). This project has space
for expansion and more exercises will be added in due to time.

This repo is here for my own nostalgic purpose and albeit there has been some changes and the repo is mainly used as a
proxy service.

A few other services that this project interacts with are:

* **[algonated-ui](https://www.github.com/mcadecio/algonated-ui)** - the user interface
* **[algonated-scales-service](https://www.github.com/mcadecio/algonated-scales-service)** - a tiny microservice
  dedicated to compile and run java code and pre-defined algorithms for the scales problem
* **[algonated-tsp-service](https://www.github.com/mcadecio/algonated-tsp-service)** - a tiny microservice dedicated to
  compile and run java code and pre-defined algorithms for the scales problem

If you would like to view the platform head over to **[algonated.herokuapp.com](https://algonated.herokuapp.com)**

The algonated-scales-service is part of a larger project Algonated. This service can be used to compile and run java
code that is constrained to a specific format for the Scales Problem.

## High-level diagram

![High Level Diagram](./img/api-hld.png)

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

```text
MIT License

Copyright (c) 2021 Dercio Daio

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
