[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/fabio-anzola/Helpupil">
    <img src="https://github.com/fabio-anzola/Helpupil/blob/master/client/src/main/resources/META-INF/resources/images/logo.png?raw=true" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Helpupil</h3>

  <p align="center">
    Helpupil is a platform where users can upload documents and make them available for other users to learn. For uploading files you get points, but only if they are marked as valid by a moderator. With these points one can compete with other users on a leaderboard. Other users of the platform can spend points to get access to documents for further education.
    <br />
    <a href="https://api.helpupil.at/v1/docs"><strong>Explore the API docs »</strong></a>
    <br />
    <br />
    <a href="https://helpupil.at/">View Demo</a>
    ·
    <a href="https://github.com/fabio-anzola/Helpupil/issues">Report Bug</a>
    ·
    <a href="https://github.com/fabio-anzola/Helpupil/issues">Request Feature</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

![LoginView](https://github.com/fabio-anzola/Helpupil/blob/master/docs/screenshots/productshot-1.png?raw=true)
![DocumentsView](https://github.com/fabio-anzola/Helpupil/blob/master/docs/screenshots/productshot-2.png?raw=true)

### Built With

* [Express](http://expressjs.com)
* [Vaadin](https://vaadin.com)
* [MongoDB](https://www.mongodb.com)
* [Swagger](https://swagger.io)

<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps:

### Prerequisites

#### API
* npm
  ```sh
  npm install npm@latest -g
  ```

#### Web-Frontend

You will need:  
1. JetBrains IntelliJ IDEA
2. Maven/An active internet connection
3. Oracle JDK / Open JDK with version 15.0.1 (or higher) 

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/fabio-anzola/Helpupil.git
   ```
2. (API) Install NPM packages
   ```sh
   cd server
   npm install
   ```

2. (Frontend) Install NPM packages
   ```sh
   cd client
   maven install (within IDE)
   ```

## Usage



## Roadmap

See the [open issues](https://github.com/fabio-anzola/Helpupil/issues) for a list of proposed features (and known issues).

## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the ... License. See `LICENSE` for more information.

## Contact

Project Link: [https://github.com/fabio-anzola/Helpupil](https://github.com/fabio-anzola/Helpupil)

## Acknowledgements

* [ANZOLA Fabio](https://github.com/fabio-anzola)
* [KRIKLER Richard](https://github.com/RichardKrikler)
* [RIGLER Tobias](https://github.com/rigler-tobias)

[contributors-shield]: https://img.shields.io/github/contributors/fabio-anzola/repo.svg?style=for-the-badge
[contributors-url]: https://github.com/fabio-anzola/repo/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/fabio-anzola/repo.svg?style=for-the-badge
[forks-url]: https://github.com/fabio-anzola/repo/network/members
[stars-shield]: https://img.shields.io/github/stars/fabio-anzola/repo.svg?style=for-the-badge
[stars-url]: https://github.com/fabio-anzola/repo/stargazers
[issues-shield]: https://img.shields.io/github/issues/fabio-anzola/repo.svg?style=for-the-badge
[issues-url]: https://github.com/fabio-anzola/repo/issues
[license-shield]: https://img.shields.io/github/license/fabio-anzola/repo.svg?style=for-the-badge
[license-url]: https://github.com/fabio-anzola/repo/blob/master/LICENSE.txt